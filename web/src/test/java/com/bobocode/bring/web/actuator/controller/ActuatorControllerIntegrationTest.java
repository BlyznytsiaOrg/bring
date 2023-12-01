package com.bobocode.bring.web.actuator.controller;

import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.web.BringWebApplication;
import com.bobocode.bring.web.server.properties.ServerProperties;
import com.bobocode.bring.web.servlet.http.HttpStatus;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ActuatorControllerIntegrationTest {

    private static final String BRING_WEB_PACKAGE = "com.bobocode.bring.web";
    public static final String URL = "http://localhost:%s%s";

    private static ServerProperties serverProperties;
    private HttpClient httpClient;

    @BeforeAll
    static void beforeAll() {
        BringApplicationContext bringApplicationContext = BringWebApplication.run(BRING_WEB_PACKAGE);
        serverProperties = bringApplicationContext.getBean(ServerProperties.class);
    }

    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newHttpClient();
    }

    @Test
    @SneakyThrows
    @DisplayName("Should call /actuator/health and return OK status")
    void shouldCallHealth() {
        //given
        String url = "/actuator/health";
        HttpRequest request = getHttpGetRequest(getHost() + url);

        //when
        HttpResponse<String> actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        //then
        assertEquals(actualResponse.statusCode(), HttpStatus.OK.getValue());
        assertThat(actualResponse.body()).isEqualTo(HttpStatus.OK.name());
    }

    @Test
    @SneakyThrows
    @DisplayName("Should call /actuator/env and return Properties")
    void shouldCallEnv() {
        //given
        String url = "/actuator/env";
        HttpRequest request = getHttpGetRequest(getHost() + url);

        //when
        HttpResponse<String> actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        //then
        assertEquals(actualResponse.statusCode(), HttpStatus.OK.getValue());
        assertThat(actualResponse.body()).isNotBlank();
    }

    @Test
    @SneakyThrows
    @DisplayName("Should call /actuator/info and return GitInfo")
    void shouldCallInfo() {
        //given
        String url = "/actuator/info";
        HttpRequest request = getHttpGetRequest(getHost() + url);

        //when
        HttpResponse<String> actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        //then
        assertEquals(actualResponse.statusCode(), HttpStatus.OK.getValue());
        assertThat(actualResponse.body()).isNotBlank();
    }

    @Test
    @SneakyThrows
    @DisplayName("Should call /actuator/loggers and change the logging level for the current package")
    void shouldCallLoggers() {
        //given
        String url = "/actuator/loggers?packageName=com.bobocode.bring.web.actuator.controller&newLevel=INFO";
        HttpRequest request = postHttpGetRequest(getHost() + url, "");

        //when
        HttpResponse<String> actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        //then
        assertEquals(actualResponse.statusCode(), HttpStatus.OK.getValue());
    }

    private String getHost() {
        return String.format(URL, serverProperties.getPort(), serverProperties.getContextPath());
    }

    @SneakyThrows
    private HttpRequest postHttpGetRequest(String url, String body) {
        return HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(new URI(url))
                .build();
    }

    @SneakyThrows
    private HttpRequest getHttpGetRequest(String url) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(new URI(url))
                .build();
    }
}
