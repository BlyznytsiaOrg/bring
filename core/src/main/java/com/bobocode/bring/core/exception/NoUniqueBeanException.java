package com.bobocode.bring.core.exception;

import java.util.Arrays;
import java.util.List;

public class NoUniqueBeanException extends RuntimeException {

    public <T> NoUniqueBeanException(Class<T> clazz) {
        super(String.format("No unique bean exception %s", clazz));
    }


    public <T> NoUniqueBeanException(Class<T> clazz, List<String> implementations) {
        super(String.format("No qualifying bean of type '%s' available: expected single matching bean but found %s: %s",
                clazz, implementations.size(), Arrays.toString(implementations.toArray())));
    }
}
