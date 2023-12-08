# PreDestroy Annotation

## Introduction

The `@PreDestroy` annotation is used to signal that the annotated method
should be executed before the container removes the instance of the class
in which the method is declared. This is typically used for cleanup operations
or releasing resources held by the instance. 

The annotated method must have no parameters, must be non-static and return void.
It is invoked before the bean is destroyed, allowing the bean to perform necessary cleanup tasks.
If multiple methods are annotated with {@code @PreDestroy} within a single
class, the order of execution is not guaranteed.

## Usage

```java
    @Component
    public class CustomerRepository {
    
      private DbConnection dbConnection;
      @PreDestroy
      public void close() {
        dbConnection.close();
      }
    }
```
The `@PreDestroy` annotated method will be automatically invoked on context closing.

## Important Points

- The method annotated with `@PreDestroy` must not have any parameters.
- This annotation is generally used in conjunction with the `@Component` stereotype annotations (e.g., `@Service`, `@Repository`, `@Controller`) or in configuration classes.

[Java Doc](https://BlyznytsiaOrg.github.io/bring-core-javadoc/com/bobocode/bring/core/annotation/PreDestroy.html)




