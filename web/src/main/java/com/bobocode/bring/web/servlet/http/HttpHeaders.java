package com.bobocode.bring.web.servlet.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The HttpHeaders class represents a collection of HTTP headers.
 * It provides methods to manage and retrieve header information.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class HttpHeaders {

    /**
     * Map to store header name-value pairs.
     */
    Map<String, String> headers;

    /**
     * List containing all header names as strings.
     */
    private List<String> headersNameList;

    /**
     * Constructs a new HttpHeaders object with an empty header map and initializes the list of header names.
     */
    public HttpHeaders() {
        this.headers = new HashMap<>();
        this.headersNameList = getAllHeadersNames();
    }

    /**
     * Retrieves the header name-value pairs.
     *
     * @return A map containing header name-value pairs.
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Retrieves a list of all header names.
     *
     * @return A list containing all header names.
     */
    public List<String> getHeadersNameList() {
        return headersNameList;
    }


    /**
     * Sets a single header with a specific value.
     *
     * @param headerName  The name of the header to set.
     * @param headerValue The value of the header.
     */
    public void set(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
    }

    /**
     * Sets a single header with multiple values, comma-separated.
     *
     * @param headerName   The name of the header to set.
     * @param headerValues A list of values for the header.
     */
    public void set(String headerName, List<String> headerValues) {
        headers.put(headerName, toCommaDelimitedString(headerValues));
    }

    /**
     * Retrieves the value of a specific header.
     *
     * @param headerName The name of the header to retrieve.
     * @return The value of the specified header.
     */
    public String get(String headerName) {
        return headers.get(headerName);
    }

    /**
     * Retrieves a list of all available header names.
     *
     * @return A list containing all available header names.
     */
    private List<String> getAllHeadersNames() {
        return Arrays.stream(Name.values())
                .map(Name::getName)
                .toList();
    }

    /**
     * Converts a list of header values to a comma-separated string.
     *
     * @param headerValues A list of header values.
     * @return A comma-separated string containing all header values.
     */
    private String toCommaDelimitedString(List<String> headerValues) {
        return headerValues.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
    }

    /**
     * Enumeration containing common HTTP header names.
     */
    public enum Name {
        ACCEPT("Accept"),
        ACCEPT_ENCODING("Accept-Encoding"),
        AUTHORIZATION("Authorization"),
        CACHE_CONTROL("Cache-Control"),
        CONNECTION("Connection"),
        CONTENT_ENCODING("Content-Encoding"),
        CONTENT_LANGUAGE("Content-Language"),
        CONTENT_LENGTH("Content-Length"),
        CONTENT_TYPE("Content-Type"),
        COOKIE("Cookie"),
        ETAG("ETag"),
        HOST("Host"),
        LAST_MODIFIED("Last-Modified"),
        LOCATION("Location"),
        SET_COOKIE("Set-Cookie");

        private final String name;

        Name(String name) {
            this.name = name;
        }

        /**
         * Retrieves the header name as a string.
         *
         * @return The name of the header.
         */
        public String getName() {
            return name;
        }
    }
}
