package com.lulumart.exception;

/**
 * Base application exception. Carries an optional HTTP status code
 * so controllers can translate it into a proper REST response.
 */
public class AppException extends RuntimeException {

    private final int statusCode;

    public AppException(String message) {
        this(message, 400);
    }

    public AppException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
    }

    public int getStatusCode() {
        return statusCode;
    }
}