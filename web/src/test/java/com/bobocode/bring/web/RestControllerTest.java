package com.bobocode.bring.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static testdata.controller.ExampleRestController.User;

import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.web.server.properties.ServerProperties;
import com.bobocode.bring.web.servlet.error.ErrorResponse;
import com.bobocode.bring.web.servlet.http.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestControllerTest {
    public static final String HELLO_PATH = "/example/hello";
    public static final String NUMBER_PATH = "/example/number";
    public static final String REQUEST_PATH = "/example/request";
    public static final String RESPONSE_PATH = "/example/response";
    public static final String BODY_AS_STRING = "/example/bodyAsString";
    public static final String BODY_AS_ENTITY = "/example/bodyAsEntity";
    public static final String NOT_EXIST_PATH = "/not-exist";
    public static final String CUSTOM_EXCEPTION_PATH = "/example/custom-exception";
    public static final String DEFAULT_EXCEPTION_PATH = "/example/default-exception";
    public static final String URL = "http://localhost:%s%s";
    private static ObjectMapper objectMapper;
    private static ServerProperties serverProperties;
    private HttpClient httpClient;

    @BeforeAll
    static void beforeAll() {
        BringApplicationContext bringApplicationContext = BringWebApplication.run("testdata");
        objectMapper = bringApplicationContext.getBean(ObjectMapper.class);
        serverProperties = bringApplicationContext.getBean(ServerProperties.class);
    }

    @BeforeEach
    void setUp() {
         httpClient = HttpClient.newHttpClient();
    }

    @Test
    @Order(1)
    @DisplayName("should return 'Hello'")
    void hello_ok() throws URISyntaxException, IOException, InterruptedException {
        String url = getHost() + HELLO_PATH;
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        assertEquals("Hello", actualResponse);
    }

    @Test
    @Order(2)
    @DisplayName("should return '200'")
    void number_ok() throws IOException, InterruptedException, URISyntaxException {
        String url = getHost() + NUMBER_PATH;
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        assertEquals("200", actualResponse);
    }

    @Test
    @Order(3)
    @DisplayName("should return 'This application has no explicit mapping for 'path'")
    void notExistingPath_notOk() throws URISyntaxException, IOException, InterruptedException {
        String url = getHost() + NOT_EXIST_PATH;
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        assertEquals(String.format("This application has no explicit mapping for '%s'",
                NOT_EXIST_PATH), actualResponse);
    }

    @Test
    @Order(4)
    @DisplayName("should return the value of path variable")
    void pathVariable_ok() throws URISyntaxException, IOException, InterruptedException {
        String pathVariable = "20";
        String url = getHost() + "/example/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        assertEquals(pathVariable, actualResponse);
    }

    @Test
    @Order(5)
    @DisplayName("should return custom message error")
    @SneakyThrows
    void shouldThrowCustomException() {
        String url = getHost() + CUSTOM_EXCEPTION_PATH;
        String defaultMessage = "Bad Request";
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

        assertEquals(errorResponse.getMessage(), defaultMessage);
        assertEquals(errorResponse.getCode(), HttpStatus.BAD_REQUEST.getValue());
    }

    @Test
    @Order(6)
    @DisplayName("should return default message error")
    @SneakyThrows
    void shouldThrowDefaultException() {
        String url = getHost() + DEFAULT_EXCEPTION_PATH;
        String defaultMessage = "TestDefaultException";
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

        assertEquals(errorResponse.getMessage(), defaultMessage);
        assertEquals(errorResponse.getCode(), HttpStatus.INTERNAL_SERVER_ERROR.getValue());
    }
    @Test
    @Order(7)
    @DisplayName("should return boolean 'true'")
    void shouldReturnBooleanTrue() throws URISyntaxException, IOException, InterruptedException {
        String pathVariable = "true";
        String url = getHost() + "/example/variable1/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        assertEquals(pathVariable, actualResponse);
    }

    @Test
    @Order(8)
    @DisplayName("should throw exception with message for invalid boolean value")
    void shouldThrowException() throws URISyntaxException, IOException, InterruptedException {
        String pathVariable = "non_boolean";
        String expectedMessage = String.format("Failed to convert value of type 'java.lang.String' "
                + "to required type 'boolean'; Invalid value [%s]", pathVariable);
        String url = getHost() + "/example/variable1/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

        assertEquals(expectedMessage, errorResponse.getMessage());
    }

    @Test
    @Order(9)
    @DisplayName("should return request parameters")
    void shouldReturnRequestParams() throws URISyntaxException, IOException, InterruptedException {
        String nameParam = "name";
        String nameValue = "Bob";
        String idParam = "id";
        String idValue = "15";
        String url = getHost() + "/example/reqParam?" + nameParam + "=" + nameValue
              + "&" + idParam + "=" + idValue;
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        assertEquals(nameValue + " - " + idValue, actualResponse);
    }

    @Test
    @Order(10)
    @DisplayName("should return exception for absense of required request parameter")
    void shouldReturnExceptionForParameterAbsense()
            throws URISyntaxException, IOException, InterruptedException {
        String nameParam = "name1";
        String nameValue = "Bob";
        String idParam = "id";
        String idValue = "15";
        String expectedMessage = "Required request parameter 'name' "
                + "for method parameter type 'String' is not present";
        String url = getHost() + "/example/reqParam?" + nameParam + "=" + nameValue
                + "&" + idParam + "=" + idValue;
        HttpRequest request = getHttpGetRequest(url);

        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);
        assertEquals(expectedMessage, errorResponse.getMessage());
    }

    @Test
    @Order(11)
    @DisplayName("should return Content-Length from HttpServletRequest")
    void request() throws URISyntaxException, IOException, InterruptedException {
        String body = "I am sending PUT request";
        int expectedValue = body.length();

        String url = getHost() + REQUEST_PATH;
        HttpRequest request = getHttpPutRequest(url, body);
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        assertEquals(expectedValue, Integer.parseInt(actualResponse));
    }

    @Test
    @Order(12)
    @DisplayName("should return Content-Type from HttpServletResponse")
    void response() throws URISyntaxException, IOException, InterruptedException {
        String expectedValue = "application/json";

        String url = getHost() + RESPONSE_PATH;
        HttpRequest request = getHttpGetRequest(url);
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        assertEquals(expectedValue, actualResponse);
    }

    @Test
    @Order(13)
    @DisplayName("should return body as String")
    void bodyAsString() throws URISyntaxException, IOException, InterruptedException {
        String body = "{\n"
                + "  \"name\": \"Bob\",\n"
                + "  \"age\": 22\n"
                + "}";

        String url = getHost() + BODY_AS_STRING;
        HttpRequest request = getHttpPostRequest(url, body);
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        assertEquals(body, actualResponse);
    }

    @Test
    @Order(14)
    @DisplayName("should return entity Object")
    void bodyAsEntity() throws URISyntaxException, IOException, InterruptedException {
        User user = new User("Bob", 20);
        String body = objectMapper.writeValueAsString(user);

        String url = getHost() + BODY_AS_ENTITY;
        HttpRequest request = getHttpPostRequest(url, body);
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        assertEquals(user.toString(), actualResponse);
    }

    private HttpRequest getHttpGetRequest(String url) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .GET()
                .uri(new URI(url))
                .build();
    }

    private HttpRequest getHttpPutRequest(String url, String body) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .uri(new URI(url))
                .build();
    }

    private HttpRequest getHttpPostRequest(String url, String body) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(new URI(url))
                .build();
    }

    private String getHost() {
        return String.format(URL, serverProperties.getPort(), serverProperties.getContextPath());
    }
}
