# ResponseEntity Class Documentation

The `ResponseEntity` class represents an HTTP response entity in the context of a Bring application. It encapsulates the response body, headers, and HTTP status, providing flexibility and control over the structure of the API response.

## Overview

The primary purpose of `ResponseEntity` is to allow developers to customize the HTTP response sent from a controller method. It becomes particularly useful when working in conjunction with the `@ResponseStatus` annotation.

**Note:** When both `ResponseEntity` and `@ResponseStatus` are used in a controller method, the HTTP status from `ResponseEntity` takes precedence over the one specified by `@ResponseStatus`.

## Generic Type Parameter

The class is generic, allowing the specification of the type of the response body using the type parameter `<T>`.

```java
public class ResponseEntity<T> {
    // class implementation
}
```

### Constructors

1. Full Constructor
```
public ResponseEntity(T body, HttpHeaders headers, HttpStatus httpStatus)
```
Constructs a new ResponseEntity with the given response body, headers, and HTTP status.

2. Constructor with Body and Status
```
public ResponseEntity(T body, HttpStatus httpStatus)
```
Constructs a new ResponseEntity with the given response body and HTTP status. Headers are set to null.

3. Constructor with Headers and Status
```
public ResponseEntity(HttpHeaders headers, HttpStatus httpStatus)
```
Constructs a new ResponseEntity with the given headers and HTTP status. The response body is set to null.

### Methods

#### `getHeaders()`
Gets the headers included in the response.

#### `getBody()`
Gets the body of the response.

#### `getHttpStatus()`
Gets the HTTP status of the response.