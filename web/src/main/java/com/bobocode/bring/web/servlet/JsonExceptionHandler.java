package com.bobocode.bring.web.servlet;

import com.bobocode.bring.web.annotation.ResponseStatus;
import com.bobocode.bring.web.dto.ErrorResponse;
import com.bobocode.bring.web.http.MediaType;
import com.bobocode.bring.web.service.ErrorResponseCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ErrorReportValve;

import java.io.PrintWriter;
import java.util.Optional;

@RequiredArgsConstructor
public class JsonExceptionHandler extends ErrorReportValve {

    private final ObjectMapper objectMapper;
    private final ErrorResponseCreator errorResponseCreator;
    private final boolean withStackTrace;

    @Override
    @SneakyThrows
    protected void report(Request request, Response response, Throwable throwable) {
        var errorResponse = Optional.of(getCause(throwable))
                .map(Throwable::getClass)
                .map(eClass -> eClass.getAnnotation(ResponseStatus.class))
                .map(responseStatus -> errorResponseCreator.prepareErrorResponse(responseStatus, throwable, withStackTrace))
                .orElseGet(() -> errorResponseCreator.defaultErrorResponse(getCause(throwable), withStackTrace));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(MediaType.UTF8_VALUE);
        response.setStatus(errorResponse.getCode());

        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(errorResponse));

        response.finishResponse();

        super.report(request, response, throwable);
    }

    private Throwable getCause(Throwable throwable) {
        return Optional.ofNullable(throwable.getCause()).orElse(throwable);
    }
}
