package com.msyrovets.scanner.controller;

import com.msyrovets.scanner.model.ScanInputData;
import com.msyrovets.scanner.model.ScanOutputData;
import com.msyrovets.scanner.service.ScanService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ScannerController {

    private final ScanService scanService;

    public ScannerController(ScanService scanService) {
        this.scanService = scanService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/")
    public String showStartPage(Model model) {
        ScanInputData scanInputData = new ScanInputData();
        model.addAttribute("scanInputData", scanInputData);
        return "start-page";
    }

    @PostMapping("/result")
    public String startScan(@Valid @ModelAttribute ScanInputData scanInputData, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "start-page";
        } else {
            List<ScanOutputData> outputData = scanService.scanInputData(scanInputData);
            model.addAttribute("outputData", outputData);
        }
        return "scanner-results";
    }

    @GetMapping("/result")
    public String startScan(Model model) {
        List<ScanOutputData> outputData = scanService.getScanData();
        model.addAttribute("outputData", outputData);
        return "scanner-results";
    }
}
