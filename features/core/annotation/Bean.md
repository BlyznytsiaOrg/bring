# Bean Annotation

**Annotation indicating that a method will be used to create `Singleton` or `Prototype` Beans.**

The result of the method invocation is an object that represents a `Bean` that can be injected into other Beans
via constructor, field injection, or setter using the `Autowired` annotation.

This annotation should be used only for methods under a configuration class
(a class annotated with `@Configuration`). When applied to a method,
it becomes eligible for `Bean` definition registration and later used for `Bean` creation.

The name of the `Bean` will be the name of the method. Injection of another `Bean` via a parameter is possible by
adding a method parameter of the type and name of a bean already defined in the `Configuration` class.

**Usage Example:**
```java
@Configuration
public class MyConfiguration {

    @Bean
    public String stringBean1() {
        return "Hello, 1";
    }

    @Bean
    public String stringBean2(String stringBean1) {
        return stringBean1 + "!";
    }
}
```

- [Java Doc](https://BlyznytsiaOrg.github.io/bring-core-javadoc/com/bobocode/bring/core/annotation/Bean.html)