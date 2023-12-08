# BeanProcessor Annotation

**Annotation used to mark extensions for custom scanners, resolvers, or processors in the client's application.**

These extensions can be used to perform specific tasks related to bean scanning, resolving, or processing.
In addition, there are some limitations on constructors. Some classes should have only the default constructor or one field,
for instance, `RestControllerBeanPostProcessor`.

**Usage Example:**
```java
@BeanProcessor
public class MyCustomProcessor {
    // Your custom processing logic here
}
```
- [Java Doc](https://BlyznytsiaOrg.github.io/bring-core-javadoc/com/bobocode/bring/core/annotation/BeanProcessor.html)