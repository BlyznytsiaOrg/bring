package com.bobocode.bring.web.servlet.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpHeaders {
    Map<String, String> headers;
    List<String> headersNameList;

    public HttpHeaders() {
        this.headers = new HashMap<>();
        this.headersNameList = getAllHeadersNames();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public List<String> getHeadersNameList() {
        return headersNameList;
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

    private List<String> getAllHeadersNames() {
       return Arrays.stream(Name.values())
                .map(Name::getName)
                .toList();
    }

    private String toCommaDelimitedString(List<String> headerValues) {
        return headerValues.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
    }


    public enum Name {
        ACCEPT("Accept"),
        ACCEPT_ENCODING ("Accept-Encoding"),
        AUTHORIZATION ("Authorization"),
        CACHE_CONTROL  ("Cache-Control"),
        CONNECTION ("Connection"),
        CONTENT_ENCODING ("Content-Encoding"),
        CONTENT_LANGUAGE ("Content-Language"),
        CONTENT_LENGTH ("Content-Length"),
        CONTENT_TYPE ("Content-Type"),
        COOKIE ("Cookie"),
        ETAG ("ETag"),
        HOST ("Host"),
        LAST_MODIFIED ("Last-Modified"),
        LOCATION ("Location"),
        SET_COOKIE ("Set-Cookie");

        private final String name;

         Name(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
