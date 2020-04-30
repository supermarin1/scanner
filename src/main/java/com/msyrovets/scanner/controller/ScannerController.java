package com.msyrovets.scanner.controller;

import com.msyrovets.scanner.model.ScanInputData;
import com.msyrovets.scanner.model.ScanOutputData;
import com.msyrovets.scanner.service.ScanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ScannerController {

    private final ScanService scanService;

    public ScannerController(ScanService scanService) {
        this.scanService = scanService;
    }

    @GetMapping("/")
    public String showStartPage(Model model) {
        ScanInputData scanInputData = new ScanInputData();
        model.addAttribute("scanInputData", scanInputData);
        return "start-page";
    }

    @PostMapping("/result")
    public String startScan(@ModelAttribute ScanInputData scanInputData, Model model) {
        List<ScanOutputData> outputData = scanService.scanInputData(scanInputData);
        model.addAttribute("outputData", outputData);
        return "scanner-results";
    }

    @GetMapping("/result")
    public String startScan(Model model) {
        List<ScanOutputData> outputData = scanService.getScanData();
        model.addAttribute("outputData", outputData);
        return "scanner-results";
    }
}
