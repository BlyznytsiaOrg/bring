# PutMapping Annotation

Annotation for mapping HTTP `PUT` requests onto specific handler methods.

## Description
This annotation is used to declare a method as a handler for HTTP `PUT` requests. It allows you to specify the URL path or pattern to which the method should respond.

### Attributes:
-`path`: The URL path or pattern for mapping HTTP `PUT` requests to the annotated method. Default value is an empty string.

**Usage Example:**
```java
 @RestController
 public class MyController implements BringServlet {
    
    @PutMapping(path = "/resource")
    public void putResource(@RequestBody UserDto dto) {
         // Your implementation logic here
    }
 }
```