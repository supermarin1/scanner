package com.msyrovets.scanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ScanInputData {
    @NotNull(message = "is required")
    @URL(message = "Please, set the URL. Example \"https://example.com/\"")
    private String url;
    @NotNull(message = "is required")
    private String targetText;
    @NotNull(message = "is required")
    private Integer maxThreadsCount;
    @NotNull(message = "is required")
    private Integer maxUrlsCount;
}
