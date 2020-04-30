package com.msyrovets.scanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ScanInputData {
    private String url;
    private String targetText;
    private Integer maxThreadsCount;
    private Integer maxUrlsCount;
}
