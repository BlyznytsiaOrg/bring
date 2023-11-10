package com.bobocode.bring.core.exception;

public class NoSuchBeanException extends RuntimeException{

    private static final String MESSAGE = "Not found bean exception %s";

    public <T> NoSuchBeanException(Class<T> clazz) {
        super(String.format(MESSAGE, clazz));
    }
}
