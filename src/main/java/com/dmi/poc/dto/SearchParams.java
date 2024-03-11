package com.dmi.poc.dto;

import lombok.Data;

@Data
public class SearchParams {
    private String query;
    private String tags;
    private String sortAttr;
    private String sortDir;
    private Integer limit;
    private Integer start;
    private String bucket;
}
