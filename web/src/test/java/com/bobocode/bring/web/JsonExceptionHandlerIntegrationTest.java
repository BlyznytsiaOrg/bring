package com.bobocode.bring.web;

import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.web.server.properties.ServerProperties;
import com.bobocode.bring.web.servlet.error.ErrorResponse;
import com.bobocode.bring.web.servlet.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.bobocode.bring.web.utils.TestUtils.getHttpGetRequest;
import static org.assertj.core.api.Assertions.assertThat;

class JsonExceptionHandlerIntegrationTest {

    public static final String CUSTOM_EXCEPTION_PATH = "/example/custom-exception";
    public static final String DEFAULT_EXCEPTION_PATH = "/example/default-exception";

    public static final String URL = "http://localhost:%s%s";

    public static final String PACKAGE = "testdata.generalintegrationtest";

    private static ObjectMapper objectMapper;
    private static ServerProperties serverProperties;

    private HttpClient httpClient;

    @BeforeAll
    static void beforeAll() {
        BringApplicationContext bringApplicationContext = BringWebApplication.run(PACKAGE);
        objectMapper = bringApplicationContext.getBean(ObjectMapper.class);
        serverProperties = bringApplicationContext.getBean(ServerProperties.class);
    }

    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newHttpClient();
    }

    @Test
    @DisplayName("should return custom message error")
    @SneakyThrows
    void shouldThrowCustomException() {
        //given
        String url = getHost() + CUSTOM_EXCEPTION_PATH;
        String defaultMessage = "Bad Request";
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

        //then
        assertThat(errorResponse.getMessage()).isEqualTo(defaultMessage);
        assertThat(errorResponse.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getValue());
    }

    @Test
    @DisplayName("should return default message error")
    @SneakyThrows
    void shouldThrowDefaultException() {
        //given
        String url = getHost() + DEFAULT_EXCEPTION_PATH;
        String defaultMessage = "TestDefaultException";
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

        //then
        assertThat(errorResponse.getMessage()).isEqualTo(defaultMessage);
        assertThat(errorResponse.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getValue());
    }

    private String getHost() {
        return String.format(URL, serverProperties.getPort(), serverProperties.getContextPath());
    }

}
