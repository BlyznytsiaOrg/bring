package com.bobocode.bring.web.server.exception;

public class WebServerException extends RuntimeException {

    public WebServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
