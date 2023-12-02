# Configuration Annotation

## Introduction

The `Configuration` annotation in Bring is used to indicate that a class is a source of bean definitions. 
Bean definitions declared within a configuration class are registered during the invocation of 
`BeanFactoryPostProcessors`. The dedicated factory post processor for configuration classes is 
`ConfigurationClassPostProcessor`.

Beans inside a configuration class are created by adding methods and annotating them with 
the `@Bean` annotation. Configuration beans must be of Singleton scope and are registered before the 
beans defined inside them.

The `@Configuration` annotation in Bring plays a crucial role in declaring bean definitions and configuring the application context. 
It allows developers to define and organize beans in a modular and maintainable way.

## Usage Example

```java
@Configuration
public class MyConfiguration {
    // Bean definitions using @Bean annotations

    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    @Bean
    public AnotherBean anotherBean() {
        return new AnotherBean();
    }
}
```
In this example, `MyConfiguration` is a configuration class annotated with `@Configuration`. 
It declares two beans (myBean and anotherBean) using methods annotated with `@Bean`. 
These beans will be registered with the Bring context during the application context initialization.

- [Java Doc](https://yevgendemotestorganization.github.io/bring-core-javadoc/com/bobocode/bring/core/annotation/Configuration.html)