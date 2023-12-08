# Scope Annotation

**Annotation indicating the visibility of a Bean. It can be applied to a class annotated with `@Component`, `@Service`, or to a method annotated with `@Bean`. It has two properties: scope name and proxy mode. The scope name is mandatory when using this annotation and can be `Singleton` or `Prototype`.**

Marking a Bean with Singleton scope means that the same object for that bean will be injected/retrieved when required. Marking a Bean with `Prototype` scope means that every time a new object for that bean will be injected/retrieved when required.

There are exceptions to when a `Prototype` Bean can return the same object: for example, when injecting a Prototype Bean into a `Singleton` Bean. In order to fix it, proxy mode can be set to `ON`.

There are two proxy modes: `ON` and `OFF`. By default, if not set explicitly, the proxy mode will be set to `OFF`. If the proxy mode is set to `ON`, then a Javassist proxy of the Bean type will be created.

**Usage Example:**
```java
@Scope(name = BeanScope.PROTOTYPE, proxyMode = ProxyMode.ON)
@Component
public class MyComponent {

}
```

- [Java Doc](https://BlyznytsiaOrg.github.io/bring-core-javadoc/com/bobocode/bring/core/annotation/Scope.html)