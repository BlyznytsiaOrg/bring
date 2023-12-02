# PatchMapping Annotation

Annotation for mapping HTTP `PATCH` requests onto specific handler methods.

## Description
This annotation is used to declare a method as a handler for HTTP `PATCH` requests. It allows you to specify the URL path or pattern to which the method should respond.

### Attributes:
-`path`: The URL path or pattern for mapping HTTP `PATCH` requests to the annotated method. Default value is an empty string.

**Usage Example:**
```java
 @RestController
 public class MyRestController implements BringServlet {
    
    @PatchMapping(path = "/resource")
    public void patchResource(@RequestBody UserDto dto) {
         // Your implementation logic here
    }
 }
```

- [Java Doc](https://yevgendemotestorganization.github.io/bring-web-javadoc/com/bobocode/bring/web/servlet/annotation/PatchMapping.html)

### See Also
- [ResponseEntity](../ResponseEntity.md)
- [Building REST API with Bring: A Quick Guide](../RestApi.md)