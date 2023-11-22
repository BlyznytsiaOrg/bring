package com.bobocode.bring.web.servlet.exception;

public class MissingRequestHeaderAnnotationValueException extends RuntimeException {
    public MissingRequestHeaderAnnotationValueException(String message) {
        super(message);
    }
}
