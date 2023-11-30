# ResponseStatus Annotation Documentation

## Purpose:

The `@ResponseStatus` annotation in the `com.bobocode.bring.web.servlet.annotation` package is used to mark a class or method with the desired HTTP response status code and an optional reason. When applied to a method or exception, the specified HTTP status code and reason will be used for the response. When both `ResponseEntity` and `@ResponseStatus` are used in a controller method, the HTTP status from `ResponseEntity` takes precedence over the one specified by `@ResponseStatus`.

## Example Usage:

### 1. Applied to a Method:

```
@ResponseStatus(HttpStatus.CREATED)
public String createResource() {
    // Method implementation
}
```
This sets the HTTP response status code to 201 (Created) for the annotated method.

### 2. Custom Exception Usage:

```
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CustomAppException extends RuntimeException {
    // Constructors...
}
```
```
@GetMapping("/example")
public String exampleEndpoint() {
    // Some logic that might throw the custom exception
    if (/* replace this with your actual condition */) {
        throw new CustomAppException("Custom exception occurred.");
    }

    // Rest of the method logic
    return "Success";
}
```

In this example, if the condition is met and the CustomAppException is thrown, the response will have a status of 500 (INTERNAL_SERVER_ERROR). You can customize the CustomAppException class to include any additional information you need in your application-specific exceptions.

Note: If a reason is provided, it will be included in the response alongside the status code.

### Additional Information:

The @ResponseStatus annotation is a convenient way to specify HTTP response status codes and reasons for methods or exceptions, allowing for better control over the HTTP responses in your web application.

### See Also
- [Building REST API with Bring: A Quick Guide](../RestApi.md)