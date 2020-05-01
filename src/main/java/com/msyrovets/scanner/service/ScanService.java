package com.msyrovets.scanner.service;

import com.msyrovets.scanner.model.ScanInputData;
import com.msyrovets.scanner.model.ScanOutputData;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.concurrent.Semaphore;

public interface ScanService {
    List<ScanOutputData> scanInputData(ScanInputData scanInputData);
    List<ScanOutputData> getScanData();
    boolean isScanningComplete();

    void setMaxLinksCount(int maxLinksCount);

    void scan(Semaphore semaphore, List<ScanOutputData> outputData, String targetText);
    void searchLinks(Document doc, List<ScanOutputData> outputData);
    boolean isContainTargetText(Document doc, String targetText);
}
