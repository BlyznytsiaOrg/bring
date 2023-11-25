package com.bobocode.bring.web.servlet.exception;

public class RequestBodyTypeUnsupportedException extends RuntimeException {
    public RequestBodyTypeUnsupportedException(String message) {
        super(message);
    }
}
