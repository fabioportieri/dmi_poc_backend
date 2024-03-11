package com.dmi.poc.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class MinioBucket {
    private String name;
    private ZonedDateTime creationDate;
}
