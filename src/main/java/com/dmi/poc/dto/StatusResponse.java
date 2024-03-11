package com.dmi.poc.dto;

public class StatusResponse {
    private String message;
    private Boolean error;

    public static StatusResponse ofSuccess(String message) {
        StatusResponse sr = new StatusResponse();
        sr.setMessage(message);
        sr.setError(false);
        return sr;
    }

    public static StatusResponse OfError(String message) {
        StatusResponse sr = new StatusResponse();
        sr.setMessage(message);
        sr.setError(true);
        return sr;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
}
