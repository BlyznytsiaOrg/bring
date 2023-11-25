package com.bobocode.bring.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.web.server.properties.ServerProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestHeaderTest {
    public static final String URL = "http://localhost:%s%s";
    public static final String PACKAGE = "testdata.requestHeader";
    public static final String HEADER = "/header";
    public static final String HEADER_NON_VALID = "/headerNonValid";
    public static final String CUSTOM = "/custom";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String CUSTOM_HEADER = "Custom";
    private static ServerProperties serverProperties;
    private HttpClient httpClient;

    @BeforeAll
    static void beforeAll() {
        BringApplicationContext bringApplicationContext = BringWebApplication.run(PACKAGE);
        serverProperties = bringApplicationContext.getBean(ServerProperties.class);
    }

    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newHttpClient();
    }

    @Test
    @DisplayName("should return Content-Type request header")
    void shouldReturnRequestHeaderContentType() throws URISyntaxException, IOException, InterruptedException {
        //given
        String url = getHost() + HEADER;
        String headerValue = APPLICATION_JSON;
        HttpRequest request = getHttpRequest(CONTENT_TYPE, headerValue, url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(headerValue);
    }

    @Test
    @DisplayName("should return empty string for not existing request header")
    void shouldReturnEmptyString() throws URISyntaxException, IOException, InterruptedException {
        //given
        String url = getHost() + HEADER_NON_VALID;
        HttpRequest request = getHttpRequest(CONTENT_TYPE, APPLICATION_JSON, url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEmpty();
    }

    @Test
    @DisplayName("should return custom header")
    void shouldReturnCustomHeader() throws URISyntaxException, IOException, InterruptedException {
        //given
        String url = getHost() + CUSTOM;
        String customHeaderValue = "bring";
        HttpRequest request = getHttpRequest(CUSTOM_HEADER, customHeaderValue, url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(customHeaderValue);
    }

    private static HttpRequest getHttpRequest(String headerName, String headerValue,
                                              String url)
            throws URISyntaxException {
        return HttpRequest.newBuilder()
                .GET()
                .header(headerName, headerValue)
                .uri(new URI(url))
                .build();
    }

    private String getHost() {
        return String.format(URL, serverProperties.getPort(), serverProperties.getContextPath());
    }
}
