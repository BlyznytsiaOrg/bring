# Controller Annotation

Indicates that the annotated class is a controller.
Controllers typically handle incoming requests, perform necessary processing, and provide a response.
This annotation can be used to mark classes as controllers within a framework or application.

## Description
This annotation is used to declare that the annotated class is a controller.
In Bring Framework it is used to annotate `StaticResourceController` class responsible for handling static resource requests.

### Attributes:
This annotation does not have additional attributes when applied to a class.

**Example:**
```java
@Controller
public class MyController {
    // Controller logic and methods
}