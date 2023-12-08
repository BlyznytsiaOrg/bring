# RequestMapping Annotation

Annotation used to indicate the mapping of a request to a specific controller class.
This annotation is applied at the class level to define the base path or URL pattern for all handler methods within the controller.

## Description
This annotation is used to declare the mapping of HTTP requests to a specific controller class. When applied at the class level, it defines the base path or URL pattern for all handler methods within the controller. If not specified, the controller will respond to requests for the base path or URL pattern.

### Attributes:
- `path`: The URL path or pattern for mapping HTTP requests to the annotated class. Default value is an empty string.

**Usage Example:**
```java
@RestController
@RequestMapping(path = "/example")
public class MyRestController implements BringServlet {
    // Your implementation logic here
}
```

- [Java Doc](https://BlyznytsiaOrg.github.io/bring-web-javadoc/com/bobocode/bring/web/servlet/annotation/RequestMapping.html)

### See Also
- [Building REST API with Bring: A Quick Guide](../RestApi.md)