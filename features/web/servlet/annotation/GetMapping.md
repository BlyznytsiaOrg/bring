# GetMapping Annotation

Annotation for mapping HTTP `GET` requests onto specific handler methods.

## Description
This annotation is used to declare a method as a handler for HTTP `GET` requests. It allows you to specify the URL path or pattern to which the method should respond.

### Attributes:
-`path`: The URL path or pattern for mapping HTTP `GET` requests to the annotated method. Default value is an empty string.

**Usage Example:**
```java
 @RestController
 public class MyRestController implements BringServlet {
    
    @GetMapping(path = "/resource/{id}")
    public ResponseEntity<Resource> getResource(@PathVariable Long id) {
         // Your implementation logic here
    }
 }
```
### See Also
[ResponseEntity](features/web/servlet/ResponseEntity.md)