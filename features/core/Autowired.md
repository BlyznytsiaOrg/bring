# Autowired Annotation

## Introduction

The `Autowired` annotation in Bring is a powerful mechanism for automatically injecting dependencies. 
It can be applied to fields, constructors, and methods. Below are examples demonstrating its usage in various scenarios.

## Example 1: Field Injection

```java
@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Other methods and properties
}
```
In this example, the UserRepository is automatically injected into UserService.

## Example 2: Constructor Injection

```java
@Service
public class OrderService {

    private final ProductService productService;

    @Autowired
    public OrderService(ProductService productService) {
        this.productService = productService;
    }

    // Other methods and properties
}
```
Here, the ProductService dependency is injected via constructor injection.

## Example 3: Method Injection

```java
@Component
public class A {

    private C c;

    @Autowired
    public void setC(C c) {
        this.c = c;
    }

    // Other methods and properties
}
```
Here, the C dependency is injected via setter method.