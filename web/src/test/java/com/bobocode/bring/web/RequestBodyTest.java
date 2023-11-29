package com.bobocode.bring.web;

import static com.bobocode.bring.web.utils.TestUtils.getHttpPostRequest;
import static org.assertj.core.api.Assertions.assertThat;

import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.web.server.properties.ServerProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testdata.requestbody.RequestBodyController.User;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class RequestBodyTest {
    public static final String URL = "http://localhost:%s%s";
    public static final String BODY_AS_STRING = "/bodyAsString";
    public static final String BODY_AS_ENTITY = "/bodyAsEntity";
    public static final String BODY_AS_MAP = "/bodyAsMap";
    public static final String BODY_AS_BYTE_ARRAY = "/bodyAsByteArray";
    public static final String PACKAGE = "testdata.requestbody";
    public static final String NAME = "Bob";
    public static final int AGE = 20;
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
    @DisplayName("should return body as String")
    void shouldReturnBodyString()
            throws URISyntaxException, IOException, InterruptedException {
        //given
        String body = "Test";
        String url = getHost() + BODY_AS_STRING;
        HttpRequest request = getHttpPostRequest(url, body);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(body);
    }

    @Test
    @DisplayName("should return Object body as String")
    void shouldReturnBodyAsStringAndCheckIt()
            throws URISyntaxException, IOException, InterruptedException {
        //given
        String body = "{  \"name\": \"Bob\",  \"age\": 20}";
        String url = getHost() + BODY_AS_STRING;
        HttpRequest request = getHttpPostRequest(url, body);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(body);
    }

    @Test
    @DisplayName("should return entity Object")
    void shouldReturnBodyAsEntity() throws URISyntaxException, IOException, InterruptedException {
        // given
        User user = new User(NAME, AGE);
        String body = objectMapper.writeValueAsString(user);
        String url = getHost() + BODY_AS_ENTITY;
        HttpRequest request = getHttpPostRequest(url, body);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(body);
    }

    @Test
    @DisplayName("should return body as Map")
    void shouldReturnMap()
            throws URISyntaxException, IOException, InterruptedException {
        //given
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", NAME);
        userMap.put("age", AGE);
        String body = objectMapper.writeValueAsString(userMap);
        String url = getHost() + BODY_AS_MAP;
        HttpRequest request = getHttpPostRequest(url, body);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(body);
    }

//    @Test
//    @DisplayName("should return byte array")
//    void shouldReturnByteArray() throws URISyntaxException, IOException, InterruptedException {
//        // given
//        String body = "byte array value";
//        String url = getHost() + BODY_AS_BYTE_ARRAY;
//        HttpRequest request = HttpRequest.newBuilder()
//                .POST(HttpRequest.BodyPublishers.ofByteArray(body.getBytes()))
//                .uri(new URI(url))
//                .build();
//        byte[] expectedValue = body.getBytes();
//
//        //when
//        byte[] body1 = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray()).body();
//
//        //then
//        assertThat(body1).isEqualTo(expectedValue);
//    }

    private String getHost() {
        return String.format(URL, serverProperties.getPort(), serverProperties.getContextPath());
    }
}
