# Prototype Bean Scope

## Introduction

In Bring Framework, a prototype-scoped bean is a bean for which a new instance is created every time it is requested. 
To declare a prototype-scoped bean, you can use the `@Scope` annotation with name property `BeanScope.PROTOTYPE`
and a proxyMode. The proxyMode is turned off by default (`ProxyMode.OFF`), but it can be turned on (`ProxyMode.ON`) 
in case a proxy is needed. For example when injecting a Prototype bean into a Singleton one.

## BeanScope Enumeration

The `BeanScope` enumeration represents the scope of beans in an application. It includes two values:

- **SINGLETON:** Indicates that a single instance of the bean is created and shared throughout the application context.
- **PROTOTYPE:** Indicates that a new instance of the bean is created whenever it is requested.

### Example Usage:

```java
@Scope(name = BeanScope.PROTOTYPE)
public class MyPrototypeBean {
    // Bean definition and methods
}
```

## ProxyMode Enumeration

The ProxyMode enumeration represents the modes of proxy functionality. It includes two values:

- **OFF:** Indicates that the proxy mode is turned off.
- **ON:** Indicates that the proxy mode is turned on.

### Example Usage:

```java
@Scope(name = BeanScope.PROTOTYPE, proxyMode = ProxyMode.ON)
public class MyPrototypeBeanWithProxy {
    // Bean definition and methods
}
```

In the above example, `ProxyMode.ON` indicates that the proxy mode is turned on for the prototype-scoped bean.
By combining the `@Scope` annotation with `ProxyMode`, you can define prototype-scoped beans, ensuring that a 
new instance is created whenever the bean is requested.