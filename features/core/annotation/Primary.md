# Primary Annotation

## Introduction

Indicates that a bean marked with this annotation is the primary candidate 
when multiple beans of the same type are available for autowiring or injection.
This annotation can be applied to a class or a method within a class to specify
it as the primary choice for dependency resolution.
When multiple beans of the same type are available, the one annotated with `@Primary`
will be given precedence for injection or autowiring. It serves as the default choice
when the bring framework needs to resolve the ambiguity between multiple candidates of the same type.

## Preconditions

There are instances where it becomes necessary to register more than one bean of the same type. 
In the provided example, we have beans named tomEmployee() and jerryEmployee(), 
both of type Employee, within a configuration class:

```java
    @Configuration
    public class Config {
    
        @Bean
        public Employee tomEmployee() {
            return new Employee("Tom");
        }
    
        @Bean
        public Employee jerryEmployee() {
            return new Employee("Jerry");
        }
    }
   ```

Attempting to run the application in this state results 
in a NoUniqueBeanException thrown by Bring. 
To resolve this issue, Bring provides a solution in the form of the `@Primary` annotation.

## Usage

1. **Use @Primary with @Bean**: Place the `@Primary` annotation on a method marked with `@Bean` annotation
    in your configuration class.

```java
    @Configuration
    public class Config {
    
        @Bean
        public Employee tomEmployee() {
            return new Employee("Tom");
        }
    
        @Bean
        @Primary
        public Employee jerryEmployee() {
            return new Employee("Jerry");
        }
    }
   ```
    
We designate the jerryEmployee() bean with the `@Primary` annotation. 
Consequently, when Bring performs injection, it will prioritize the jerryEmployee() bean over the tomEmployee().

Next, initiate the application context and retrieve the Employee bean from it:

```
        BringApplicationContext bringApplicationContext = BringApplication.run(Config.class);
        Employee employee = bringApplicationContext.getBean(Employee.class);
        assertThat(employee.getName()).isEqualTo("Jerry");
   ```
The assertion reveals a preference for the jerryEmployee() instance during the autowiring process.

2. **Use @Primary with @Component**: Place the `@Primary` annotation on a class marked with `@Component` annotation.
We have the option to apply the `@Primary` annotation directly to the beans. Let's examine the scenario presented below:


```java
    public interface I {
    }
   ```
We have a I interface and two subclass beans A and B:

```java
    @Component
    @Primary
    public class A implements I {
    }

   ```
```java
    @Component
    public class B implements I {
    } 

   ```
It's important to highlight that we annotate the A bean with @Primary.
Let’s create a bean to use dependency injection while finding the right bean:

```java
    @Component
    @Getter
    public class C {
  
        @Autowired
        private I field;
        
}
   ```
In this case, both the A and B beans are candidates for autowiring.
Since we designated the A bean with `@Primary`, it will be chosen for dependency injection:

```
    BringApplicationContext bringApplicationContext = BringApplication.run(Config.class);
    C c = bringApplicationContext.getBean(C.class);
    assertThat(c.getField()).isInstanceOf(A.class);

   ```
The assertion reveals a preference for the A instance during the autowiring process.

[Java Doc](https://BlyznytsiaOrg.github.io/bring-core-javadoc/com/bobocode/bring/core/annotation/Primary.html)
