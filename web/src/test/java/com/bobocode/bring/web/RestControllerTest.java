package com.bobocode.bring.web;

import static com.bobocode.bring.web.utils.TestUtils.getHttpGetRequest;
import static com.bobocode.bring.web.utils.TestUtils.getHttpPutRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static testdata.generalIntegrationTest.ExampleRestController.User;

import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.web.server.properties.ServerProperties;
import com.bobocode.bring.web.servlet.error.ErrorResponse;
import com.bobocode.bring.web.servlet.http.HttpStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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

public class RestControllerTest {
    public static final String HELLO_PATH = "/example/hello";
    public static final String NUMBER_PATH = "/example/number";
    public static final String REQUEST_PATH = "/example/request";
    public static final String RESPONSE_PATH = "/example/response";
    public static final String NOT_EXIST_PATH = "/not-exist";
    public static final String STATUS = "/example/status";
    public static final String CUSTOM_EXCEPTION_PATH = "/example/custom-exception";
    public static final String DEFAULT_EXCEPTION_PATH = "/example/default-exception";
    public static final String URL = "http://localhost:%s%s";
    public static final String APPLICATION_JSON = "application/json";
    private static ObjectMapper objectMapper;
    private static ServerProperties serverProperties;
    private HttpClient httpClient;

    @BeforeAll
    static void beforeAll() {
        BringApplicationContext bringApplicationContext = BringWebApplication.run("testdata.generalIntegrationTest");
        objectMapper = bringApplicationContext.getBean(ObjectMapper.class);
        serverProperties = bringApplicationContext.getBean(ServerProperties.class);
    }

    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newHttpClient();
    }

    @Test
    @DisplayName("should return 'Hello'")
    void shouldReturnHello() throws URISyntaxException, IOException, InterruptedException {
        //given
        String expectedValue = "Hello";
        String url = getHost() + HELLO_PATH;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("should return '200'")
    void number_ok() throws IOException, InterruptedException, URISyntaxException {
        //given
        String expectedValue = "200";
        String url = getHost() + NUMBER_PATH;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("should return 'This application has no explicit mapping for 'path'")
    void shouldThrowExceptionOnInvalidMapping() throws URISyntaxException, IOException, InterruptedException {
        //given
        String url = getHost() + NOT_EXIST_PATH;
        HttpRequest request = getHttpGetRequest(url);
        String expectedValue = String.format("This application has no explicit mapping for '%s'",
                NOT_EXIST_PATH);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

        //then
        assertThat(errorResponse.getMessage()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("should return custom message error")
    @SneakyThrows
    void shouldThrowCustomException() {
        //given
        String url = getHost() + CUSTOM_EXCEPTION_PATH;
        String defaultMessage = "Bad Request";
        HttpRequest request = getHttpGetRequest(url);

        //then
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

        //when
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

        // then
        assertThat(errorResponse.getMessage()).isEqualTo(defaultMessage);
        assertThat(errorResponse.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getValue());
    }

    @Test
    @DisplayName("should return Content-Length from HttpServletRequest")
    void shouldUseHttpServletRequest() throws URISyntaxException, IOException, InterruptedException {
        //given
        String body = "I am sending PUT request";
        int expectedValue = body.length();
        String url = getHost() + REQUEST_PATH;
        HttpRequest request = getHttpPutRequest(url, body);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(Integer.parseInt(actualResponse)).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("should return Content-Type from HttpServletResponse")
    void shouldUseHttpServletResponse() throws URISyntaxException, IOException, InterruptedException {
        //given
        String url = getHost() + RESPONSE_PATH;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(APPLICATION_JSON);
    }

    @Test
    @DisplayName("should set a response status")
    void shouldReturnResponseStatus() throws URISyntaxException, IOException, InterruptedException {
        //given
        String url = getHost() + STATUS;
        HttpRequest request = getHttpGetRequest(url);

        //when
        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        int actualStatusCode = response.statusCode();

        //then
        assertThat(actualStatusCode).isEqualTo(HttpStatus.ACCEPTED.getValue());
    }

    @Test
    @DisplayName("should except several arguments")
    void shouldExceptSeveralArguments() throws IOException, URISyntaxException, InterruptedException {
        //given
        String pathVariable = "10";
        String url = getHost() + "/example/severalArg/" + pathVariable;
        String userName = "Ann";
        int userAge = 30;
        User user = new User(userName, userAge);
        String body = objectMapper.writeValueAsString(user);
        String headerNameContentType = "Content-Type";
        String headerValueContentValue = APPLICATION_JSON;
        String headerNameAccept = "Accept";
        String headerValueAccept = "xml";
        String expectedResponseStatus = "200";
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header(headerNameContentType, headerValueContentValue)
                .header(headerNameAccept, headerValueAccept)
                .uri(new URI(url))
                .build();

        //when
        String actualResponse = httpClient
                .send(request, HttpResponse.BodyHandlers.ofString())
                .body();
        JsonNode jsonNode = objectMapper.readValue(actualResponse, JsonNode.class);

        String actualId = jsonNode.get("id").asText();
        String actualValueOfAcceptHeader = jsonNode.get(headerNameAccept).asText();
        JsonNode userJson = jsonNode.get("user");
        String actualUserName = userJson.get("name").asText();
        int actualUserAge = userJson.get("age").asInt();
        String actualValueForHeaderContentType = jsonNode.get(headerNameContentType).asText();
        String actualStatus = jsonNode.get("status").asText();

        //then
        assertThat(actualId).isEqualTo(pathVariable);
        assertThat(actualValueOfAcceptHeader).isEqualTo(headerValueAccept);
        assertThat(actualUserName).isEqualTo(userName);
        assertThat(actualUserAge).isEqualTo(userAge);
        assertThat(actualValueForHeaderContentType).isEqualTo(headerValueContentValue);
        assertThat(actualStatus).isEqualTo(expectedResponseStatus);
    }

    private String getHost() {
        return String.format(URL, serverProperties.getPort(), serverProperties.getContextPath());
    }
}
