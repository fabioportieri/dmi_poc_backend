package com.dmi.poc.dto;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
public class MinioItem {
    private Long size;
    private String name;
    private Map<String, String> metadata;
    private ZonedDateTime lastModified;
    private String storageClass;
    private String version;
    private String ownerDisplayName;
    private String url;
    private Boolean isDir;
}
