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


    @Override
    public List<ScanOutputData> scanInputData(ScanInputData scanInputData) {
        outputData.clear();

        Semaphore semaphore = new Semaphore(scanInputData.getMaxThreadsCount(), true);
        maxLinksCount = scanInputData.getMaxUrlsCount();
        String targetText = scanInputData.getTargetText();

        //add first link to the list with WAIT status
        ScanOutputData scanOutputData = new ScanOutputData();
        scanOutputData.setUrl(scanInputData.getUrl());
        scanOutputData.setScanStatus(ScanStatus.WAITING);
        outputData.add(scanOutputData);
        maxLinksCount--;

        //start scan
        scan(semaphore, outputData, targetText);

        return outputData;
    }

    @Override
    public List<ScanOutputData> getScanData() {
        return outputData;
    }

    @Override
    public boolean isScanningComplete() {
        boolean hasNoWaiting = outputData.stream().noneMatch(data -> data.getScanStatus().equals(ScanStatus.WAITING));
        boolean hasNoProcess= outputData.stream().noneMatch(data -> data.getScanStatus().equals(ScanStatus.PROCESS));
        return hasNoWaiting && hasNoProcess;
    }

    @Override
    public void setMaxLinksCount(int maxLinksCount) {
        this.maxLinksCount = maxLinksCount;
    }

    public void scan(Semaphore semaphore, List<ScanOutputData> outputData, String targetText) {
        synchronized (outputData) {
            List<ScanOutputData> waitList = outputData
                    .stream()
                    .filter(data -> data.getScanStatus().equals(ScanStatus.WAITING))
                    .filter(data -> !data.isChecking())
                    .collect(Collectors.toList());

            if (waitList.size() != 0) {
                waitList.forEach(
                        scanOutputData -> startNewLinkCheckThread(semaphore, scanOutputData, targetText, outputData));
            }
        }
    }

    private void startNewLinkCheckThread(Semaphore semaphore, ScanOutputData data, String targetText,
                                         List<ScanOutputData> outputData) {
        String url = data.getUrl();

        data.setChecking(true);

        new Thread(() -> {
            try {
                semaphore.acquire();
                    data.setScanStatus(ScanStatus.PROCESS);

                    try {
                        Document doc = Jsoup.connect(url).get();

                        //search links
                        searchLinks(doc, outputData);

                        //search text
                        data.setIsTargetTextFound(isContainTargetText(doc, targetText));
                        data.setScanStatus(ScanStatus.SUCCESS);
                    } catch (IOException e) {
                        data.setScanStatus(ScanStatus.FAILED);
                        data.setErrorMessage("Can't connect. " + e.getMessage());
                    }
            } catch (InterruptedException e) {
                data.setScanStatus(ScanStatus.FAILED);
                data.setErrorMessage("Can't connect. " + e.getMessage());
            } finally {
                semaphore.release();
            }

            scan(semaphore, outputData, targetText);

        }).start();
    }

    public void searchLinks(Document doc, List<ScanOutputData> outputData) {
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
    }


    @Override
    public boolean isContainTargetText(Document doc, String targetText) {
        Elements elements = doc.select("body:contains(" + targetText + ")");
        return elements.size() > 0;
    }

}
