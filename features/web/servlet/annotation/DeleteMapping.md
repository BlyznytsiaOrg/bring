# DeleteMapping Annotation

Annotation for mapping HTTP `DELETE` requests onto specific handler methods.

## Description
This annotation is used to declare a method as a handler for HTTP `DELETE` requests. It allows you to specify the URL path or pattern to which the method should respond.

### Attributes:
-`path`: The URL path or pattern for mapping HTTP `DELETE` requests to the annotated method. Default value is an empty string.

**Usage Example:**
```java
@RestController
public class MyRestController implements BringServlet {

    @DeleteMapping(path = "/resource/{id}")
    public void deleteResource(@PathVariable Long id) {
        // Your implementation logic here
    }
}
```

### See Also
[ResponseEntity](features/web/servlet/ResponseEntity.md)