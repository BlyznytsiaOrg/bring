package com.bobocode.bring.web.service;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.web.annotation.ResponseStatus;
import com.bobocode.bring.web.dto.ErrorResponse;
import com.bobocode.bring.web.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class ErrorResponseCreator {

    public ErrorResponse prepareErrorResponse(ResponseStatus responseStatus,
                                              Throwable throwable,
                                              Boolean withStackTrace) {
        return ErrorResponse.builder()
                .code(responseStatus.value().getValue())
                .status(responseStatus.value().getReasonPhrase())
                .message(getMessage(responseStatus, throwable))
                .timestamp(LocalDateTime.now())
                .stackTrace(getStackTrace(throwable, withStackTrace))
                .build();
    }

    public ErrorResponse defaultErrorResponse(Throwable e, Boolean withStackTrace) {
        return ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.getValue())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .stackTrace(getStackTrace(e, withStackTrace))
                .build();
    }

    private String getStackTrace(Throwable throwable, Boolean withStackTrace) {
        return Boolean.TRUE.equals(withStackTrace) ? prepareStackTrace(throwable) : null;
    }

    private String prepareStackTrace(Throwable throwable) {
        return Arrays.stream(throwable.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String getMessage(ResponseStatus responseStatus, Throwable throwable) {
        return !responseStatus.reason().isEmpty() ? responseStatus.reason() : throwable.getMessage();
    }
}
