# Value Annotation

**Indicates that the annotated element is to be resolved to a value. This annotation is typically used to inject values into fields, method parameters, constructor parameters, or method return values in Bring applications.**

The value provided in this annotation represents the key or expression to resolve from property sources or other value providers.

**Example usage in a Bring component:**
```java
public class MyComponent {
    @Value("${my.property}")
    private String myPropertyValue;

    // Methods and logic using myPropertyValue
}
```

- [Java Doc](https://yevgendemotestorganization.github.io/bring-core-javadoc/com/bobocode/bring/core/annotation/Value.html)