# Singleton Bean Scope

## Introduction

In Bring Framework, a singleton-scoped bean is a bean for which a single instance is created and shared throughout 
the application context. To declare a singleton-scoped bean, you can use the `@Scope` annotation with name property 
`BeanScope.SINGLETON` or without setting the name property in the annotation as Singleton is the default bean scope.

## BeanScope Enumeration

The `BeanScope` enumeration represents the scope of beans in an application. It includes two values:

- **SINGLETON:** Indicates that a single instance of the bean is created and shared throughout the application context.
- **PROTOTYPE:** Indicates that a new instance of the bean is created whenever it is requested.

### Example Usage:

```java
@Scope(name = BeanScope.SINGLETON)
public class MySingletonBean {
    // Bean definition and methods
}

@Scope
public class MySingletonBean {
    // Bean definition and methods
}
```