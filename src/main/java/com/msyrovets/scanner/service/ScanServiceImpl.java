package com.msyrovets.scanner.service;

import com.msyrovets.scanner.enums.ScanStatus;
import com.msyrovets.scanner.model.ScanInputData;
import com.msyrovets.scanner.model.ScanOutputData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScanServiceImpl implements ScanService {
    private List<ScanOutputData> outputData = new ArrayList<>();

    @Override
    public List<ScanOutputData> scanInputData(ScanInputData scanInputData) {
        outputData = new ArrayList<>();

        //todo fake data for start
        ScanOutputData scanOutputData = new ScanOutputData();
        scanOutputData.setUrl(scanInputData.getUrl());
        scanOutputData.setScanStatus(ScanStatus.SUCCESS);
        scanOutputData.setIsTargetTestFound(Boolean.TRUE);
        scanOutputData.setErrorMessage("no errors");

        outputData.add(scanOutputData);

        return outputData;
    }

    @Override
    public List<ScanOutputData> getScanData() {
        //todo fake data for start
        ScanOutputData scanOutputData = new ScanOutputData();
        scanOutputData.setUrl("second url");
        scanOutputData.setScanStatus(ScanStatus.FAILED);
        scanOutputData.setIsTargetTestFound(Boolean.FALSE);
        scanOutputData.setErrorMessage("can't reach the site");

        outputData.add(scanOutputData);

        return outputData;
    }
}
