package com.bobocode.bring.core.exception;

public class PostConstructException extends RuntimeException {
    public static String POST_CONSTRUCT_EXCEPTION_MESSAGE ="@PostConstruct should be added to method without parameters";
    public PostConstructException(Throwable cause) {
        super(POST_CONSTRUCT_EXCEPTION_MESSAGE, cause);
    }
}
