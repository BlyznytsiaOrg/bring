package com.bobocode.bring.web;

import static com.bobocode.bring.web.utils.TestUtils.getHttpGetRequest;
import static org.assertj.core.api.Assertions.assertThat;

import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.web.server.properties.ServerProperties;
import com.bobocode.bring.web.servlet.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testdata.pathvariable.PathVariableController;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PathVariableTest {

    public static final String URL = "http://localhost:%s%s";
    public static final String PACKAGE = "testdata.pathvariable";
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
    @DisplayName("should return String value of path variable")
    void shouldReturnStringValue() throws URISyntaxException, IOException, InterruptedException {
        //given
        String pathVariable = "string";
        String url = getHost() + "/str/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(pathVariable);
    }

    @Test
    @DisplayName("should return Long value of path variable")
    void shouldReturnLongValue() throws URISyntaxException, IOException, InterruptedException {
        //given
        long pathVariable = 10;
        String url = getHost() + "/long/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        //then
        assertThat(actualResponse).isEqualTo(Long.class.getSimpleName());
    }

    @Test
    @DisplayName("should return Long value of path variable")
    void shouldReturnLongAsStringValue() throws URISyntaxException, IOException, InterruptedException {
        //given
        long pathVariable = 10;
        String url = getHost() + "/longAsString/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(String.valueOf(pathVariable));
    }

    @Test
    @DisplayName("should throw exception of path variable")
    void shouldThrowExceptionForInvalidValue() throws URISyntaxException, IOException, InterruptedException {
        //given
        String expectedMessage = "Failed to convert value of type 'java.lang.String' "
                + "to required type 'java.lang.Long'; Invalid value [not-long]";
        String pathVariable = "not-long";
        String url = getHost() + "/long/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

        // then
        assertThat(errorResponse.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("should return Integer value of path variable")
    void shouldReturnInteger() throws URISyntaxException, IOException, InterruptedException {
        //given
        int pathVariable = 15;
        String url = getHost() + "/integer/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(Integer.class.getSimpleName());
    }

    @Test
    @DisplayName("should return Double value of path variable")
    void shouldReturnDouble() throws URISyntaxException, IOException, InterruptedException {
        //given
        double pathVariable = 15.02;
        String url = getHost() + "/double/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(Double.class.getSimpleName());
    }

    @Test
    @DisplayName("should return Float value of path variable")
    void shouldReturnFloat() throws URISyntaxException, IOException, InterruptedException {
        //given
        float pathVariable = 15.02f;
        String url = getHost() + "/float/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(Float.class.getSimpleName());
    }

    @Test
    @DisplayName("should return Short value of path variable")
    void shouldReturnShort() throws URISyntaxException, IOException, InterruptedException {
        //given
        short pathVariable = 350;
        String url = getHost() + "/short/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(Short.class.getSimpleName());
    }

    @Test
    @DisplayName("should return Byte value of path variable")
    void shouldReturnByte() throws URISyntaxException, IOException, InterruptedException {
        //given
        byte pathVariable = 127;
        String url = getHost() + "/byte/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(Byte.class.getSimpleName());
    }

    @Test
    @DisplayName("should return Character value of path variable")
    void shouldReturnChar() throws URISyntaxException, IOException, InterruptedException {
        //given
        char pathVariable = 'a';
        String url = getHost() + "/char/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        char actualResponseChar = actualResponse.charAt(0);
        //then
        assertThat(actualResponseChar).isEqualTo(pathVariable);
    }

    @Test
    @DisplayName("should return boolean 'true'")
    void shouldReturnBooleanTrue() throws URISyntaxException, IOException, InterruptedException {
        //given
        String pathVariable = "true";
        String url = getHost() + "/boolean/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(pathVariable);
    }

    @Test
    @DisplayName("should throw exception with message for invalid boolean value")
    void shouldThrowExceptionOnBoolean() throws URISyntaxException, IOException, InterruptedException {
        //given
        String pathVariable = "non_boolean";
        String expectedMessage = String.format("Failed to convert value of type 'java.lang.String' "
                + "to required type 'boolean'; Invalid value [%s]", pathVariable);
        String url = getHost() + "/boolean/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        // when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

        // then
        assertThat(errorResponse.getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("should throw exception with message for invalid boolean value")
    void shouldThrowExceptionOnUnsupportedArgType() throws URISyntaxException, IOException, InterruptedException {
        //given
        PathVariableController.User user = new PathVariableController.User("Bob", 22);
        String pathVariable = "invalid";
        String expectedMessage = String.format("The type parameter: '%s' is not supported",
                user.getClass().getName());
        String url = getHost() + "/invalidType/" + pathVariable;
        HttpRequest request = getHttpGetRequest(url);

        // when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);

        // then
        assertThat(errorResponse.getMessage()).isEqualTo(expectedMessage);
    }

    private String getHost() {
        return String.format(URL, serverProperties.getPort(), serverProperties.getContextPath());
    }
}
