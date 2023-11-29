# @PostConstruct

## Introduction

In the Bring framework, `@PostConstruct` is a method-level annotation used to indicate that a method should be invoked immediately after an instance of the bean is constructed, and before any other initialization logic occurs.
## Usage

To use `@PostConstruct`, follow these steps:

1. **Add the Annotation**: Place the `@PostConstruct` annotation on a method within your Bring bean class.

    ```java
    import com.bobocode.bring.core.annotation.PostConstruct;

    public class MyBean {

        @PostConstruct
        public void init() {
            // Initialization logic here
            // This method will be called after bean instantiation.
        }
    }
    ```


2. **Invoke the Bring Container**: Make sure that you obtain the bean from the Bring container. The `@PostConstruct` annotated method will be automatically invoked.

    ```java
    import com.bobocode.bring.core.BringApplication;

    public class MyApp {
        public static void main(String[] args) {
             var bringApplicationContext = BringApplication.run("your.path");
             var myBean = bringApplicationContext.getBean(CustomPostConstruct.class);
                // Your bean is now initialized, and @PostConstruct method has been called.
            }
        }
    ```

## Important Points

- The method annotated with `@PostConstruct` must not have any parameters.
- This annotation is generally used in conjunction with the `@Component` stereotype annotations (e.g., `@Service`, `@Repository`, `@Controller`) or in configuration classes.
- The `@PostConstruct` method will be invoked after the bean has been constructed and before any custom initialization logic specified in the bean definition.

## Example

Here is a simple example of a class using `@PostConstruct`:

```java
import com.bobocode.bring.core.annotation.PostConstruct;

public class ExampleBean {

    private String message;

    @PostConstruct
    public void init() {
        message = "Hello, this is an example!";
        // Additional initialization logic
    }

    public String getMessage() {
        return message;
    }
}
