# PutMapping Annotation

Annotation for mapping HTTP `PUT` requests onto specific handler methods.

## Description
This annotation is used to declare a method as a handler for HTTP `PUT` requests. It allows you to specify the URL path or pattern to which the method should respond.

### Attributes:
-`path`: The URL path or pattern for mapping HTTP `PUT` requests to the annotated method. Default value is an empty string.

**Usage Example:**
```java
 @RestController
 public class MyRestController implements BringServlet {
    
    @PutMapping(path = "/resource/{id}")
    public void updateResource(@PathVariable Long id, 
                               @RequestBody UserDto dto) {
         // Your implementation logic here
    }
 }
```

- [Java Doc](https://yevgendemotestorganization.github.io/bring-web-javadoc/com/bobocode/bring/web/servlet/annotation/PutMapping.html)

### See Also
- [ResponseEntity](../ResponseEntity.md)
- [Building REST API with Bring: A Quick Guide](../RestApi.md)