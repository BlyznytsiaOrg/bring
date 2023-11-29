# Setter Injection in Bring

In Bring, setter injection is another approach to provide dependencies to a class, but instead of using the constructor, it utilizes setter methods.

When Bring creates an instance of a class using setter injection, it invokes specific setter methods and supplies the required dependencies through these methods.

###  Example:

```
@Component
public class MyClass {
    private MyDependency dependency;

    @Autowired
    public void setDependency(MyDependency dependency) {
        this.dependency = dependency;
    }

    // Other class methods...
}


```

### In this example:

- MyClass contains a setter method setDependency, annotated with `@Autowired`, to set the MyDependency instance.
- Bring identifies this annotated setter method as the injection point and uses it to supply the necessary dependency.

Unlike constructor injection, in setter injection, the presence of `@Autowired` explicitly marks the method as an injection point in Bring. 
This annotation instructs Bring to use the annotated setter method for dependency injection when instantiating the class.


### Note

If you're interested in exploring examples of setter injection in Bring, you can refer to the test case `BringSetterInjectionTest`.
This tests case typically demonstrates how Bring instantiates classes and injects dependencies through setter.