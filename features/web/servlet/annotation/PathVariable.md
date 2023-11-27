# PathVariable Annotation

Annotation used to indicate that a method parameter should be bound to a URI template variable.

## Description
This annotation is used to declare that a method parameter should be bound to a URI template variable. It is commonly used with other annotations like `GetMapping`, `DeleteMapping`, etc., to extract values from the request URI.

### Attributes:
- `value`: The name of the URI template variable to bind to the method parameter. Default value is an empty string.

**Usage Example:**
```java
@RestController
public class MyController implements BringServlet {

    @GetMapping(path = "/resource/{id}")
    public Resource getResource(@PathVariable Long id) {
        // Your implementation logic here
    }
}
```