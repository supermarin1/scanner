package com.msyrovets.scanner.model;

import com.msyrovets.scanner.enums.ScanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanOutputData {
    private String url;
    private ScanStatus scanStatus;
    private Boolean isTargetTextFound;
    private String errorMessage;
}
