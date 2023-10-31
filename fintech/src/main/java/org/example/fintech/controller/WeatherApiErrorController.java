package org.example.fintech.controller;

import lombok.Getter;

/**
 * Custom exception class representing errors that can occur while interacting with the Weather API.
 */

@Getter
public class WeatherApiErrorController extends RuntimeException {

    private final int httpStatusCode;
    private final int errorCode;
    private final String errorMessage;

    public WeatherApiErrorController(int httpStatusCode, int errorCode, String errorMessage) {
        super(errorMessage);
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
