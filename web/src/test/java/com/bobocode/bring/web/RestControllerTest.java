package com.bobocode.bring.web;

import static org.assertj.core.api.Assertions.assertThat;
import static testdata.controller.ExampleRestController.User;

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
    public static final String BODY_AS_STRING = "/example/bodyAsString";
    public static final String BODY_AS_ENTITY = "/example/bodyAsEntity";
    public static final String NOT_EXIST_PATH = "/not-exist";
    public static final String HEADER = "/example/header";
    public static final String STATUS = "/example/status";
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
    @DisplayName("should return 'Hello'")
    void hello_ok() throws URISyntaxException, IOException, InterruptedException {
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
    void notExistingPath_notOk() throws URISyntaxException, IOException, InterruptedException {
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
    @DisplayName("should return the value of path variable")
    void pathVariable_ok() throws URISyntaxException, IOException, InterruptedException {
        //given
        String pathVariable = "20";
        String url = getHost() + "/example/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(pathVariable);
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
    @DisplayName("should return boolean 'true'")
    void shouldReturnBooleanTrue() throws URISyntaxException, IOException, InterruptedException {
        //given
        String pathVariable = "true";
        String url = getHost() + "/example/variable1/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(pathVariable);
    }

    @Test
    @DisplayName("should throw exception with message for invalid boolean value")
    void shouldThrowException() throws URISyntaxException, IOException, InterruptedException {
        //given
        String pathVariable = "non_boolean";
        String expectedMessage = String.format("Failed to convert value of type 'java.lang.String' "
                + "to required type 'boolean'; Invalid value [%s]", pathVariable);
        String url = getHost() + "/example/variable1/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        // when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

        // then
        assertThat(errorResponse.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("should return request parameters")
    void shouldReturnRequestParams() throws URISyntaxException, IOException, InterruptedException {
        //given
        String nameParam = "name";
        String nameValue = "Bob";
        String idParam = "id";
        String idValue = "15";
        String expectedValue = nameValue + " - " + idValue;
        String url = getHost() + "/example/reqParam?" + nameParam + "=" + nameValue
                + "&" + idParam + "=" + idValue;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("should return exception for absense of required request parameter")
    void shouldReturnExceptionForParameterAbsense()
            throws URISyntaxException, IOException, InterruptedException {
        //given
        String nameParam = "name1";
        String nameValue = "Bob";
        String idParam = "id";
        String idValue = "15";
        String expectedMessage = "Required request parameter 'name' "
                + "for method parameter type 'String' is not present";
        String url = getHost() + "/example/reqParam?" + nameParam + "=" + nameValue
                + "&" + idParam + "=" + idValue;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);
        String actualValue = errorResponse.getMessage();

        //then
        assertThat(actualValue).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("should return Content-Length from HttpServletRequest")
    void request() throws URISyntaxException, IOException, InterruptedException {
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
    void response() throws URISyntaxException, IOException, InterruptedException {
        //given
        String expectedValue = "application/json";
        String url = getHost() + RESPONSE_PATH;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("should return body as String")
    void shouldReturnBodyAsStringAndCheckIt()
            throws URISyntaxException, IOException, InterruptedException {
        //given
        String body = "{  \"name\": \"Bob\",  \"age\": 22}";
        String url = getHost() + BODY_AS_STRING;
        HttpRequest request = getHttpPostRequest(url, body);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(body);
    }

    @Test
    @DisplayName("should return entity Object")
    void bodyAsEntity() throws URISyntaxException, IOException, InterruptedException {
        // given
        User user = new User("Bob", 20);
        String body = objectMapper.writeValueAsString(user);
        String url = getHost() + BODY_AS_ENTITY;
        HttpRequest request = getHttpPostRequest(url, body);
        String expectedValue = user.toString();

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("should return request header")
    void header() throws URISyntaxException, IOException, InterruptedException {
        //given
        String url = getHost() + HEADER;
        String headerName = "Content-Type";
        String headerValue = "application/json";
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header(headerName, headerValue)
                .uri(new URI(url))
                .build();

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(headerValue);
    }

    @Test
    @DisplayName("should set a response status")
    void status() throws URISyntaxException, IOException, InterruptedException {
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
    @DisplayName("should return several arguments")
    void several() throws IOException, URISyntaxException, InterruptedException {
        //given
        String pathVariable = "10";
        String url = getHost() + "/example/severalArg/" + pathVariable;
        String userName = "Ann";
        int userAge = 30;
        User user = new User(userName, userAge);
        String body = objectMapper.writeValueAsString(user);
        String headerNameContentType = "Content-Type";
        String headerValueContentValue = "application/json";
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
