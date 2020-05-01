package com.msyrovets.scanner.model;

import com.msyrovets.scanner.enums.ScanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScanOutputData {
    private String url;
    private ScanStatus scanStatus;
    private Boolean isTargetTextFound;
    private String errorMessage;
    private boolean isChecking;
}
