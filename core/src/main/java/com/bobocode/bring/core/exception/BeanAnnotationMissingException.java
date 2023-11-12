package com.bobocode.bring.core.exception;

public class BeanAnnotationMissingException extends RuntimeException {

    public BeanAnnotationMissingException(String message, Object... args) {
        super(String.format(message, args));
    }
    
}
