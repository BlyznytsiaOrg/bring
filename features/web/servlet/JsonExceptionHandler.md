# JsonExceptionHandler

[Java Doc](https://yevgendemotestorganization.github.io/bring-web-javadoc/com/bobocode/bring/web/servlet/JsonExceptionHandler.html)

## Overview

**Exception Handler:** This component specializes in managing exceptions by intercepting and customizing error responses in JSON format. It extends `ErrorReportValve` to provide tailored error handling within a Servlet container.

The `JsonExceptionHandler` is responsible for capturing exceptions thrown during request processing and generating JSON-formatted error responses. It employs an `ObjectMapper` for efficient JSON processing, an `ErrorResponseCreator` to construct error responses, and relies on `ServerProperties` for configuration settings.

By overriding the default error reporting method, this component ensures precise interception and customization of error handling. The overridden method orchestrates the setting of headers, content type, character encoding, and response status for the generated JSON error response.

The preparation of the JSON error response body is based on the exception thrown, and utility methods are provided to fetch the root cause of the exception and set headers for the JSON error response.

## Dependencies

- `ErrorReportValve`
- `ErrorResponse`
- `ObjectMapper`
- `ErrorResponseCreator`
- `ServerProperties`

## Methods

### `report(Request request, Response response, Throwable throwable)`

Overrides the default error reporting method to intercept and customize error handling.

- **Parameters:**
    - `request`: The incoming servlet request.
    - `response`: The outgoing servlet response.
    - `throwable`: The thrown exception to be handled.

### `setErrorResponse(Response response, Throwable throwable)`

Sets the JSON error response in the servlet response.

- **Parameters:**
    - `response`: The outgoing servlet response.
    - `throwable`: The thrown exception.

### `setHeaders(Response response, ErrorResponse errorResponse)`

Sets the headers in the servlet response for the JSON error response.

- **Parameters:**
    - `response`: The outgoing servlet response.
    - `errorResponse`: The JSON error response.

### `prepareBody(Throwable throwable)`

Prepares the JSON body of the error response based on the thrown exception.

- **Parameters:**
    - `throwable`: The thrown exception.

### `getCause(Throwable throwable)`

Gets the root cause of the given throwable.

- **Parameters:**
    - `throwable`: The thrown exception.

## Details

- The `JsonExceptionHandler` class extends `ErrorReportValve` to customize error handling in a Servlet container.
- The `report` method overrides the default error reporting to intercept and handle exceptions.
- The `setErrorResponse` method converts the exception into a structured JSON response and sets it in the servlet response.
- The `setHeaders` method configures the response headers for the JSON error response.
- The `prepareBody` method constructs the JSON body of the error response based on the thrown exception.
- The `getCause` method retrieves the root cause of the given exception.

---