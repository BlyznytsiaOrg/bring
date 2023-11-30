# PostMapping Annotation

Annotation for mapping HTTP `POST` requests onto specific handler methods.

## Description
This annotation is used to declare a method as a handler for HTTP `POST` requests. It allows you to specify the URL path or pattern to which the method should respond.

### Attributes:
-`path`: The URL path or pattern for mapping HTTP `POST` requests to the annotated method. Default value is an empty string.

**Usage Example:**
```java
 @RestController
 public class MyRestController implements BringServlet {
    
    @PostMapping(path = "/resource")
    public void saveResource(@RequestBody UserDto dto) {
         // Your implementation logic here
    }
 }
```

### See Also
- [ResponseEntity](../ResponseEntity.md)
- [Building REST API with Bring: A Quick Guide](../RestApi.md)