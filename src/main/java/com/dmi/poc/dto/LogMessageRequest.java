package com.dmi.poc.dto;

import lombok.Data;

@Data
public class LogMessageRequest {
    private String operation;
    private String message;
}
