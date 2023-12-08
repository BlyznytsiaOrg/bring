# NoConstructorWithAutowiredAnnotationBeanException

**Exception thrown to indicate that no constructor with the `Autowired` annotation is found for a bean class within a Bring Dependency Injection framework.**

## Constructor

### `NoConstructorWithAutowiredAnnotationBeanException(Class<T> clazz, String listOfConstructors)`

Constructs a new `NoConstructorWithAutowiredAnnotationBeanException` with a message indicating the absence of a constructor with the Autowired annotation for the specified class.

- **Parameters:**
    - `clazz`: The class for which no constructor with Autowired annotation is found.
    - `listOfConstructors`: String representation of the list of constructors found for the class (to guide adding Autowired annotation to one of them).

[Java Doc](https://BlyznytsiaOrg.github.io/bring-core-javadoc/com/bobocode/bring/core/exception/NoConstructorWithAutowiredAnnotationBeanException.html)