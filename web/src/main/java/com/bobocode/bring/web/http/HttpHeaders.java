package com.bobocode.bring.web.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpHeaders {
    public static final String ACCEPT = "Accept";

    public static final String AUTHORIZATION = "Authorization";

    public static final String CACHE_CONTROL = "Cache-Control";

    public static final String CONNECTION = "Connection";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String COOKIE = "Cookie";
    public static final String ETAG = "ETag";
    public static final String HOST = "Host";
    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String LOCATION = "Location";
    public static final String SET_COOKIE2 = "Set-Cookie2";

    Map<String, String> headers = new HashMap<>();

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void set(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
    }

    public void set(String headerName, List<String> headerValues) {
        headers.put(headerName, toCommaDelimitedString(headerValues));
    }

    public String get(String headerName) {
        return headers.get(headerName);
    }

    private String toCommaDelimitedString(List<String> headerValues) {
        return headerValues.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
    }
}
