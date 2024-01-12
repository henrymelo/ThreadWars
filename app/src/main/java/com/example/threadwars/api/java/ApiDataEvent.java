package com.example.threadwars.api.java;

public class ApiDataEvent {
    private final String apiData;

    public ApiDataEvent(String apiData) {
        this.apiData = apiData;
    }

    public String getApiData() {
        return apiData;
    }
}