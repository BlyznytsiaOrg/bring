package com.bobocode.bring.core.exception;

import java.util.Arrays;
import java.util.Set;

public class CyclicBeanException extends RuntimeException {

    private static final String MESSAGE = "Looks like you have cyclic dependency between those beans %s";

    public CyclicBeanException(Set<String> classes) {
        super(String.format(MESSAGE, Arrays.toString(classes.toArray())));
    }
}
