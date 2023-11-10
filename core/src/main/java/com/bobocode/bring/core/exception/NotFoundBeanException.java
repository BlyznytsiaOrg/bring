package com.bobocode.bring.core.exception;

public class NotFoundBeanException extends RuntimeException {

    private static final String MESSAGE = "Not found bean exception %s";

    public <T> NotFoundBeanException(Class<T> clazz) {
        super(String.format(MESSAGE, clazz));
    }
}
