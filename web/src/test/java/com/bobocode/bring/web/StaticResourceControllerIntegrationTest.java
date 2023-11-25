package com.bobocode.bring.web;

import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.web.server.properties.ServerProperties;
import com.bobocode.bring.web.servlet.StaticResourceController;
import com.bobocode.bring.web.servlet.error.ErrorResponse;
import com.bobocode.bring.web.servlet.http.HttpHeaders.Name;
import com.bobocode.bring.web.servlet.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.bobocode.bring.web.RestControllerTest.URL;
import static com.bobocode.bring.web.servlet.http.MediaType.TEXT_HTML_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test class for the {@link StaticResourceController}.
 * Verifies the behavior of the controller when handling static resource requests.
 *
 * @author Blyzhnytsia Team
 * @version 1.0
 * @since 2023-11-22
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StaticResourceControllerIntegrationTest {

    // Message template for static file not found error
    private static final String STATIC_FILE_NOT_FOUND_MESSAGE = "Can't find the File: %s.";
    public static final String PACKAGE = "testdata.generalIntegrationTest";

    // ObjectMapper for JSON processing
    private static ObjectMapper objectMapper;

    // ServerProperties for configuration
    private static ServerProperties serverProperties;

    // HttpClient for making HTTP requests
    private HttpClient httpClient;

    /**
     * Initializes the necessary dependencies before all test methods.
     */
    @BeforeAll
    static void beforeAll() {
        BringApplicationContext bringApplicationContext = BringWebApplication.run(PACKAGE);
        objectMapper = bringApplicationContext.getBean(ObjectMapper.class);
        serverProperties = bringApplicationContext.getBean(ServerProperties.class);
    }

    /**
     * Set up the HttpClient before each test method.
     */
    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newHttpClient();
    }

    /**
     * Tests that the controller successfully returns a static file.
     */
    @Test
    @DisplayName("Should successfully return a static file")
    @SneakyThrows
    void shouldReturnStaticFile() {
        String url = "/static/index.html";
        HttpRequest request = getHttpGetRequest(getHost() + url);

        HttpResponse<String> actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(actualResponse.statusCode(), HttpStatus.OK.getValue());
        assertTrue(actualResponse.headers().map().get(Name.CONTENT_TYPE.getName()).contains(TEXT_HTML_VALUE));
    }

    /**
     * Tests that the controller does not return a static file when the file is not found.
     */
    @Test
    @DisplayName("Should not return a static file when the file is not found")
    @SneakyThrows
    void shouldNotReturnStaticFile() {
        String url = "/static/wrongFolder/index.html";
        String errorMessage = String.format(STATIC_FILE_NOT_FOUND_MESSAGE, url);
        HttpRequest request = getHttpGetRequest(getHost() + url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST.getValue(), errorResponse.getCode());
        assertEquals(errorMessage, errorResponse.getMessage());
    }

    /**
     * Constructs the base URL for making HTTP requests.
     *
     * @return The base URL for making HTTP requests.
     */
    private String getHost() {
        return String.format(URL, serverProperties.getPort(), serverProperties.getContextPath());
    }

    /**
     * Constructs an HTTP GET request with the specified URL.
     *
     * @param url The URL for the HTTP GET request.
     * @return The constructed HTTP GET request.
     */
    @SneakyThrows
    private HttpRequest getHttpGetRequest(String url) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(new URI(url))
                .build();
    }
}
