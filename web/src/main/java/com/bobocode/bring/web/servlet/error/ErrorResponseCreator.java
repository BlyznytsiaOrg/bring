package com.bobocode.bring.web.servlet.error;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.web.servlet.annotation.ResponseStatus;
import com.bobocode.bring.web.servlet.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The ErrorResponseCreator class is responsible for creating instances of ErrorResponse.
 * It provides methods to prepare error responses with various configurations.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Component
public class ErrorResponseCreator {

    /**
     * Prepares a customized error response based on the provided parameters.
     *
     * @param responseStatus The response status to be included in the error response.
     * @param throwable The Throwable object associated with the error, providing additional details.
     * @param withStackTrace A flag indicating whether to include the stack trace in the error response.
     * @return An ErrorResponse object containing the prepared error details.
     */
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

    /**
     * Prepares a default error response for internal server errors.
     *
     * @param e The Throwable object representing the internal server error.
     * @param withStackTrace A flag indicating whether to include the stack trace in the error response.
     * @return An ErrorResponse object containing the default internal server error details.
     */
    public ErrorResponse defaultErrorResponse(Throwable e, Boolean withStackTrace) {
        return ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.getValue())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .stackTrace(getStackTrace(e, withStackTrace))
                .build();
    }

    /**
     * Returns the stack trace as a string based on the specified flag.
     *
     * @param throwable The Throwable object from which the stack trace is extracted.
     * @param withStackTrace A flag indicating whether to include the stack trace.
     * @return The stack trace as a string if withStackTrace is true, otherwise null.
     */
    private String getStackTrace(Throwable throwable, Boolean withStackTrace) {
        return Boolean.TRUE.equals(withStackTrace) ? prepareStackTrace(throwable) : null;
    }

    /**
     * Prepares the stack trace as a string.
     *
     * @param throwable The Throwable object from which the stack trace is extracted.
     * @return The stack trace as a string.
     */
    private String prepareStackTrace(Throwable throwable) {
        return Arrays.stream(throwable.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    /**
     * Retrieves the error message based on the provided ResponseStatus and Throwable.
     *
     * @param responseStatus The ResponseStatus object representing the error status.
     * @param throwable The Throwable object associated with the error.
     * @return The error message, either from ResponseStatus or Throwable.
     */
    private String getMessage(ResponseStatus responseStatus, Throwable throwable) {
        return !responseStatus.reason().isEmpty() ? responseStatus.reason() : throwable.getMessage();
    }
}
