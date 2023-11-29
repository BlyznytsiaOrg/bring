# RequestHeader Annotation

Annotation used to indicate that a method parameter should be bound to a specific HTTP request header.

## Description
This annotation is used to declare that a method parameter should be bound to a specific HTTP request header. It is commonly used with the `@PostMapping` annotation to access values from the request headers.

### Attributes:
- `value`: The name of the HTTP request header to bind to the method parameter. Default value is an empty string.

**Usage Example:**
```java
@RestController
public class MyController implements BringServlet {

    @PostMapping(path = "/resource")
    public void postResource(@RequestHeader @RequestHeader(value = "Content-Length") Long header,
                             @RequestBody UserDto dto) {
        // Your implementation logic here
    }
}
```