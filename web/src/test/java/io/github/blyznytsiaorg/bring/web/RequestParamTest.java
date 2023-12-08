package io.github.blyznytsiaorg.bring.web;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blyznytsiaorg.bring.core.context.impl.BringApplicationContext;
import io.github.blyznytsiaorg.bring.web.server.properties.ServerProperties;
import io.github.blyznytsiaorg.bring.web.servlet.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.blyznytsiaorg.bring.web.utils.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class RequestParamTest {

    public static final String URL = "http://localhost:%s%s";
    public static final String PACKAGE = "testdata.requestparam";
    public static final String NAME_PARAM = "name";
    public static final String NAME_VALUE = "Bob";
    public static final String EQUALS = "=";
    public static final String ID_PARAM = "id";
    public static final String ID_VALUE = "20";
    public static final String AND = "&";
    public static final String QUESTION_MARK = "?";
    public static final String NAME_PARAM_INVALID = "name_invalid";
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
    @DisplayName("should return String request parameter")
    void shouldReturnRequestParamsString() throws URISyntaxException, IOException, InterruptedException {
        //given
        String url = getHost() + "/str" + QUESTION_MARK + NAME_PARAM + EQUALS + NAME_VALUE;
        HttpRequest request = TestUtils.getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(NAME_VALUE);
    }

    @Test
    @DisplayName("should return Long request parameter")
    void shouldReturnRequestParamsLong() throws URISyntaxException, IOException, InterruptedException {
        //given
        String url = getHost() + "/long" + QUESTION_MARK + ID_PARAM + EQUALS + ID_VALUE;
        HttpRequest request = TestUtils.getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        assertThat(actualResponse).isEqualTo(ID_VALUE);
    }

    @Test
    @DisplayName("should return request parameters")
    void shouldReturnRequestParams() throws URISyntaxException, IOException, InterruptedException {
        //given
        String expectedValue = NAME_VALUE + " - " + ID_VALUE;
        String url = getHost() + QUESTION_MARK + NAME_PARAM + EQUALS + NAME_VALUE
                + AND + ID_PARAM + EQUALS + ID_VALUE;
        HttpRequest request = TestUtils.getHttpGetRequest(url);

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
        String expectedMessage = "Required request parameter 'name' "
                + "for method parameter type 'String' is not present";
        String url = getHost() + QUESTION_MARK + NAME_PARAM_INVALID + EQUALS + NAME_VALUE
                + AND + ID_PARAM + EQUALS + ID_VALUE;
        HttpRequest request = TestUtils.getHttpGetRequest(url);

        //when
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        ErrorResponse errorResponse = objectMapper.readValue(actualResponse, ErrorResponse.class);
        String actualValue = errorResponse.getMessage();

        //then
        assertThat(actualValue).isEqualTo(expectedMessage);
    }

    private String getHost() {
        return String.format(URL, serverProperties.getPort(), serverProperties.getContextPath());
    }
}
