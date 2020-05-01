package com.msyrovets.scanner.service;

import com.msyrovets.scanner.enums.ScanStatus;
import com.msyrovets.scanner.model.ScanOutputData;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ScanServiceImplTest {

    private ScanService scanService;

    @BeforeEach
    void setUp() {
        scanService = new ScanServiceImpl();
    }

    @Test
    public void searchLinkTest() {
        List<ScanOutputData> outputData = scanService.getScanData();

        File inputWithOne = new File("src/test/resources/html/oneLink.html");
        File inputWithFive = new File("src/test/resources/html/fiveLinks.html");
        File inputWithout = new File("src/test/resources/html/noLink.html");
        try {
            Document docWithOne = Jsoup.parse(inputWithOne, "UTF-8");
            scanService.setMaxLinksCount(5);
            scanService.searchLinks(docWithOne, outputData);
            assertThat(outputData.size() == 1).isTrue();

            outputData.clear();
            Document docWithFive = Jsoup.parse(inputWithFive, "UTF-8");
            scanService.setMaxLinksCount(5);
            scanService.searchLinks(docWithFive, outputData);
            assertThat(outputData.size() == 5).isTrue();

            outputData.clear();
            scanService.setMaxLinksCount(3);
            scanService.searchLinks(docWithFive, outputData);
            assertThat(outputData.size() == 3).isTrue();

            outputData.clear();
            Document docWithout = Jsoup.parse(inputWithout, "UTF-8");
            scanService.setMaxLinksCount(5);
            scanService.searchLinks(docWithout, outputData);
            assertThat(outputData.size() == 0).isTrue();

        } catch (IOException e) {
            log.error("Can't parse html {}", e.getMessage());
        }
    }

    @Test
    public void maxLinksCounterTest() {
        List<ScanOutputData> outputData = scanService.getScanData();

        File inputWithFive = new File("src/test/resources/html/fiveLinks.html");

        try {
            outputData.clear();
            Document docWithFive = Jsoup.parse(inputWithFive, "UTF-8");
            scanService.setMaxLinksCount(5);
            scanService.searchLinks(docWithFive, outputData);
            assertThat(outputData.size() == 5).isTrue();

            outputData.clear();
            scanService.setMaxLinksCount(3);
            scanService.searchLinks(docWithFive, outputData);
            assertThat(outputData.size() == 3).isTrue();

            outputData.clear();
            scanService.setMaxLinksCount(2);
            scanService.searchLinks(docWithFive, outputData);
            assertThat(outputData.size() == 2).isTrue();
        } catch (IOException e) {
            log.error("Can't parse html {}", e.getMessage());
        }
    }

    @Test
    public void searchTextTest() {
        File input = new File("src/test/resources/html/noLink.html");
        try {
            Document docWithOne = Jsoup.parse(input, "UTF-8");
            assertThat(scanService.isContainTargetText(docWithOne, "link")).isTrue();
            assertThat(scanService.isContainTargetText(docWithOne, "paragraph")).isTrue();
            assertThat(scanService.isContainTargetText(docWithOne, "head")).isFalse();
        } catch (IOException e) {
            log.error("Can't parse html {}", e.getMessage());
        }

    }

    @Test
    public void scanTest() throws InterruptedException {
        List<ScanOutputData> outputDataList = scanService.getScanData();

        Semaphore semaphore = new Semaphore(3, true);
        String targetText = "https://bash.im/";
        scanService.setMaxLinksCount(99); //actual will be 100

        outputDataList.add(ScanOutputData.builder().url("https://bash.im/").scanStatus(ScanStatus.WAITING).build());

        scanService.scan(semaphore, outputDataList, targetText);

        while(!scanService.isScanningComplete()) {
            Thread.sleep(2000);
        }

        assertThat(scanService.getScanData().size() == 100).isTrue();
        assertThat(scanService.getScanData().stream().noneMatch(data -> data.getScanStatus().equals(ScanStatus.PROCESS))).isTrue();
        assertThat(scanService.getScanData().stream().noneMatch(data -> data.getScanStatus().equals(ScanStatus.WAITING))).isTrue();
    }
}