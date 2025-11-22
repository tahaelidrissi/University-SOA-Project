package com.university.rest.dto;

public class SuccessResponse {
    private String message;
    private Object data;
    private long timestamp;

    public SuccessResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public SuccessResponse(String message) {
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public SuccessResponse(String message, Object data) {
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters et Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}