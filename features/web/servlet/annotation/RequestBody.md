# RequestBody Annotation

Annotation used to indicate that a method parameter should be bound to the body of the HTTP request.

## Description
This annotation is used to declare that a method parameter should be bound to the body of the HTTP request. It is commonly used with the `@PostMapping` annotation to handle `POST` requests where the data is sent in the request body.

### Attributes:
This annotation does not have additional attributes when applied to a method parameter.


**Usage Example:**
```java
@RestController
public class MyRestController implements BringServlet {

    @PostMapping(path = "/resource")
    public void postResource(@RequestBody UserDto dto) {
        // Your implementation logic here
    }
}
```