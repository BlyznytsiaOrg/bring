package com.bobocode.bring.web.servlet.exception;

public class RequestPathDuplicateException extends RuntimeException {
    public RequestPathDuplicateException(String message) {
        super(message);
    }
}
