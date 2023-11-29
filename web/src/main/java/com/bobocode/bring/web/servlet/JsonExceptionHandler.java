package com.bobocode.bring.web.servlet;

import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.web.server.properties.ServerProperties;
import com.bobocode.bring.web.servlet.annotation.ResponseStatus;
import com.bobocode.bring.web.servlet.error.ErrorResponse;
import com.bobocode.bring.web.servlet.error.ErrorResponseCreator;
import com.bobocode.bring.web.servlet.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ErrorReportValve;

import java.util.Optional;

/**
 * Component for handling exceptions and generating JSON error responses. Extends {@link ErrorReportValve} to intercept
 * and customize error handling in a Servlet container.
 *
 * @author Blyzhnytsia Team
 * @since  1.0
 */
@RequiredArgsConstructor
public class JsonExceptionHandler extends ErrorReportValve {

    // ObjectMapper for JSON processing
    private final ObjectMapper objectMapper;

    // Creator for constructing error responses
    private final ErrorResponseCreator errorResponseCreator;

    // ServerProperties for configuration
    private final ServerProperties serverProperties;

    /**
     * Overrides the default error reporting method to intercept and customize error handling.
     *
     * @param request   The incoming servlet request.
     * @param response  The outgoing servlet response.
     * @param throwable The thrown exception to be handled.
     */
    @Override
    protected void report(Request request, Response response, Throwable throwable) {
        Optional.ofNullable(throwable).ifPresent(thr -> setErrorResponse(response, thr));

        super.report(request, response, throwable);
    }

    /**
     * Sets the JSON error response in the servlet response.
     *
     * @param response  The outgoing servlet response.
     * @param throwable The thrown exception.
     */
    @SneakyThrows
    private void setErrorResponse(Response response, Throwable throwable) {
        var errorResponse = prepareBody(throwable);
        setHeaders(response, errorResponse);
        response.getWriter().println(objectMapper.writeValueAsString(errorResponse));
        response.finishResponse();
    }

    /**
     * Sets the headers in the servlet response for the JSON error response.
     *
     * @param response      The outgoing servlet response.
     * @param errorResponse The JSON error response.
     */
    private static void setHeaders(Response response, ErrorResponse errorResponse) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(MediaType.UTF8_VALUE);
        response.setStatus(errorResponse.getCode());
    }

    /**
     * Prepares the JSON body of the error response based on the thrown exception.
     *
     * @param throwable The thrown exception.
     * @return The prepared JSON error response.
     */
    private ErrorResponse prepareBody(Throwable throwable) {
        var withStackTrace = serverProperties.isWithStackTrace();
        var cause = getCause(throwable);
        return Optional.of(cause)
                .map(Throwable::getClass)
                .map(eClass -> eClass.getAnnotation(ResponseStatus.class))
                .map(responseStatus -> errorResponseCreator.prepareErrorResponse(responseStatus, cause, withStackTrace))
                .orElseGet(() -> errorResponseCreator.defaultErrorResponse(cause, withStackTrace));
    }

    /**
     * Gets the root cause of the given throwable.
     *
     * @param throwable The thrown exception.
     * @return The root cause of the exception.
     */
    private Throwable getCause(Throwable throwable) {
        return Optional.ofNullable(throwable.getCause()).orElse(throwable);
    }
}
