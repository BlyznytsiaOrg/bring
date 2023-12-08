package io.github.blyznytsiaorg.bring.web;

import io.github.blyznytsiaorg.bring.core.context.impl.BringApplicationContext;
import io.github.blyznytsiaorg.bring.web.server.properties.ServerProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testdata.mixconfigurationandrestcontroller.dto.UserRequest;
import testdata.mixconfigurationandrestcontroller.dto.UserResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static io.github.blyznytsiaorg.bring.web.RestControllerTest.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

class MixConfigurationBeanAndRestControllerTest {

    public static final String SHORTEN_PATH = "/api/shorten";

    public static final String PACKAGE = "testdata.mixconfigurationandrestcontroller";

    public static final String URL = "http://localhost:%s%s";
    public static final String CONTENT_TYPE = "Content-Type";

    private static ServerProperties serverProperties;
    private static ObjectMapper objectMapper;
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
    @DisplayName("Should return model with one field")
    void shouldReturnModelWithOneField() throws URISyntaxException, IOException, InterruptedException {
        //given
        String url = getHost() + SHORTEN_PATH;

        var userRequest = new UserRequest();
        userRequest.setOriginalUrl("http://localhost:9009/test");
        String body = objectMapper.writeValueAsString(userRequest);

        //when
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .uri(new URI(url))
                .build();
        String actualResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();

        //then
        var userResponse = objectMapper.readValue(actualResponse, UserResponse.class);
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getShortenedUrl()).isEqualTo(userRequest.getOriginalUrl());
    }

    private String getHost() {
        return String.format(URL, serverProperties.getPort(), serverProperties.getContextPath());
    }
}
