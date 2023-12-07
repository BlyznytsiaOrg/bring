package io.github.blyznytsiaorg.bring.web.servlet.http;

/**
 * Represents an HTTP response entity, containing a response body, headers, and HTTP status.
 * When both {@code ResponseEntity} and {@code @ResponseStatus} are used in a controller method,
 * the HTTP status from {@code ResponseEntity} takes precedence over the one specified by {@code @ResponseStatus}.
 *
 * @param <T> The type of the response body.
 */
public class ResponseEntity<T> {

    private final HttpHeaders headers;
    private final T body;
    private final HttpStatus httpStatus;

    /**
     * Constructs a new ResponseEntity with the given body, headers, and HTTP status.
     *
     * @param body       The response body.
     * @param headers    The headers to be included in the response.
     * @param httpStatus The HTTP status of the response.
     */
    public ResponseEntity(T body, HttpHeaders headers, HttpStatus httpStatus) {
        this.body = body;
        this.headers = headers;
        this.httpStatus = httpStatus;
    }


    /**
     * Constructs a new ResponseEntity with the given body and HTTP status.
     *
     * @param body       The response body.
     * @param httpStatus The HTTP status of the response.
     */
    public ResponseEntity(T body, HttpStatus httpStatus) {
        this(body, null, httpStatus);
    }

    /**
     * Constructs a new ResponseEntity with the given headers and HTTP status.
     *
     * @param headers    The headers to be included in the response.
     * @param httpStatus The HTTP status of the response.
     */
    public ResponseEntity(HttpHeaders headers, HttpStatus httpStatus) {
        this(null, headers, httpStatus);
    }

    /**
     * Gets the headers included in the response.
     *
     * @return The headers of the response.
     */
    public HttpHeaders getHeaders() {
        return headers;
    }

    /**
     * Gets the body of the response.
     *
     * @return The response body.
     */
    public T getBody() {
        return body;
    }

    /**
     * Gets the HTTP status of the response.
     *
     * @return The HTTP status.
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
