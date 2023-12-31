# RequestParam Annotation

Annotation used to indicate that a method parameter should be bound to a query parameter in a web request.

## Description
This annotation is used to declare that a method parameter should be bound to a query parameter in a web request. It is commonly used with the `@GetMapping` annotation to extract values from the query parameters.

### Attributes:
This annotation does not have additional attributes when applied to a method parameter.

**Usage Example:**
```java
@RestController
public class MyRestController implements BringServlet {

    @GetMapping(path = "/resource")
    public ResponseEntity<Resource> getResource(@RequestParam Long id, @RequestParam String name) {
        // Your implementation logic here
    }
}
```

- [Java Doc](https://BlyznytsiaOrg.github.io/bring-web-javadoc/com/bobocode/bring/web/servlet/annotation/RequestParam.html)

### See Also
- [Building REST API with Bring: A Quick Guide](../RestApi.md)