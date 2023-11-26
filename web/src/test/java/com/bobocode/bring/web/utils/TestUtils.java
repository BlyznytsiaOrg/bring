package com.bobocode.bring.web.utils;

import lombok.experimental.UtilityClass;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

@UtilityClass
public final class TestUtils {

    public static HttpRequest getHttpGetRequest(String url) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .GET()
                .uri(new URI(url))
                .build();
    }

    public static HttpRequest getHttpPutRequest(String url, String body) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .uri(new URI(url))
                .build();
    }

    public static HttpRequest getHttpPostRequest(String url, String body) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(new URI(url))
                .build();
    }
}
