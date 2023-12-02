# Service Annotation

**Indicates that an annotated class is a *Bring component*. This annotation serves as a marker for Bring to automatically detect and register the annotated class as a bean during component scanning.**

By default, the name of the bean will be the simple name of the annotated class. You can customize the bean name by providing a value to the `value` attribute. If no value is provided, the bean name will be generated based on the class name with the initial letter in lowercase.

This annotation is part of *the Bring Framework's* component scanning mechanism, which allows for automatic discovery and registration of Bring beans.

**Usage Example:**
```java
@Service
public class MyComponent {
    // Class definition
}

// Custom bean name
@Service("customBeanName")
public class AnotherComponent {
    // Class definition
}
```

- [Java Doc](https://yevgendemotestorganization.github.io/bring-core-javadoc/com/bobocode/bring/core/annotation/Service.html)