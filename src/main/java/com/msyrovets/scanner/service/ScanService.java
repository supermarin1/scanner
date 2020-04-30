package com.msyrovets.scanner.service;

import com.msyrovets.scanner.model.ScanInputData;
import com.msyrovets.scanner.model.ScanOutputData;

import java.util.List;

public interface ScanService {
    List<ScanOutputData> scanInputData(ScanInputData scanInputData);
    List<ScanOutputData> getScanData();
}
