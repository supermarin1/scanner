package com.msyrovets.scanner.service;

import com.msyrovets.scanner.enums.ScanStatus;
import com.msyrovets.scanner.model.ScanInputData;
import com.msyrovets.scanner.model.ScanOutputData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

@Service
public class ScanServiceImpl implements ScanService {
    private final List<ScanOutputData> outputData = new ArrayList<>();
    private int maxLinksCount;

    private Semaphore semaphore;


    @Override
    public List<ScanOutputData> scanInputData(ScanInputData scanInputData) {
        semaphore = new Semaphore(scanInputData.getMaxThreadsCount(), true);
        maxLinksCount = scanInputData.getMaxUrlsCount();

        //add first link to the list with WAIT status
        ScanOutputData scanOutputData = new ScanOutputData();
        scanOutputData.setUrl(scanInputData.getUrl());
        scanOutputData.setScanStatus(ScanStatus.WAITING);
        outputData.add(scanOutputData);
        maxLinksCount--;

        scan();

        return outputData;
    }

    @Override
    public List<ScanOutputData> getScanData() {
        return outputData;
    }

    public void scan() {
        synchronized (outputData) {
            List<ScanOutputData> waitList = outputData
                    .stream()
                    .filter(data -> data.getScanStatus().equals(ScanStatus.WAITING))
                    .collect(Collectors.toList());
            if (waitList.size() != 0) {
                waitList.forEach(this::startNewLinkCheckThread);
            }
        }
    }

    private void startNewLinkCheckThread(ScanOutputData data) {
        String url = data.getUrl();

        new Thread(() -> {
            try {
                semaphore.acquire();
                if (data.getScanStatus().equals(ScanStatus.WAITING)) {
                    data.setScanStatus(ScanStatus.PROCESS);
                    try {
                        Document doc = Jsoup.connect(url).get();

                        //search links
                        Elements links = doc.select("a");
                        synchronized (outputData) {
                            for (Element link : links) {
                                if (maxLinksCount != 0) {
                                    String linkUrl = link.attr("abs:href");
                                    //add new waiting data
                                    if (!linkUrl.isEmpty()) {
                                        ScanOutputData scanOutputDataToWait = new ScanOutputData();
                                        scanOutputDataToWait.setUrl(linkUrl);
                                        scanOutputDataToWait.setScanStatus(ScanStatus.WAITING);
                                        outputData.add(scanOutputDataToWait);
                                        maxLinksCount--;
                                    }
                                }
                            }
                        }

                        // todo search target text
                        data.setScanStatus(ScanStatus.SUCCESS);
                    } catch (IOException e) {
                        data.setScanStatus(ScanStatus.FAILED);
                        data.setErrorMessage("Can't connect. " + e.getMessage());
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }

            //start new scan
            scan();

        }).start();
    }
}
