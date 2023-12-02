# Order Annotation

**`@Order` defines the sort order for an annotated component.**

The `value` is optional and represents an order value for objects that should be orderable, for example, in a Collection. Lower values have higher priority. The default value is `Integer.MAX_VALUE`, indicating the lowest priority (losing to any other specified order value).

**Usage Example:**
```java
@Order(1)
public class MyOrderedComponent {
    // Class definition
}
```

- [Java Doc](https://yevgendemotestorganization.github.io/bring-core-javadoc/com/bobocode/bring/core/annotation/Order.html)