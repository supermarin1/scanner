package com.msyrovets.scanner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ScannerController {

    @GetMapping("/")
    public String showStartPage(){
        return "start-page";
    }
}
