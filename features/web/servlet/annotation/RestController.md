# RestController Annotation

Indicates that the annotated class is a specialized type of controller designed for RESTful services.
REST controllers typically handle HTTP requests, process them, and generate HTTP responses suitable for REST APIs.
This annotation can be used to mark classes as REST controllers within a framework or application.

## Description
This annotation is used to declare that the annotated class is a specialized type of controller designed for RESTful services. REST controllers typically handle HTTP requests, process them, and generate HTTP responses suitable for REST APIs.

### Attributes:
This annotation does not have additional attributes when applied to a class.

**Usage Example:**
```java
@RestController
public class MyRestController implements BringServlet {
    // REST controller logic and methods
}
```

**NOTE:** The class annotated with `@RestController`  should implement marker interface `BringServlet` to be recognized as controller by the framework.

- [Java Doc](https://BlyznytsiaOrg.github.io/bring-web-javadoc/com/bobocode/bring/web/servlet/annotation/RestController.html)

### See Also
- [BringServlet](../BringServlet.md)
- [Building REST API with Bring: A Quick Guide](../RestApi.md)