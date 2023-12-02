# NoUniqueBeanException

**Exception thrown to indicate that no unique bean is available or multiple beans match the expected type within a Bring Dependency Injection framework.**

## Constructors

### `NoUniqueBeanException(Class<T> clazz)`

Constructs a new `NoUniqueBeanException` with a message indicating that no unique bean of the specified type is available.

- **Parameters:**
    - `clazz`: The class type for which no unique bean is found.

### `NoUniqueBeanException(String beanName)`

Constructs a new `NoUniqueBeanException` with a message indicating that a bean with the given name already exists.

- **Parameters:**
    - `beanName`: The name of the bean that already exists.

### `NoUniqueBeanException(Class<T> clazz, List<String> implementations)`

Constructs a new `NoUniqueBeanException` with a message indicating that no qualifying bean of a certain type is available.

- **Parameters:**
     - `clazz`: The class type for which no qualifying bean is found.
     - `implementations`: The list of implementations found for the specified type.

[Java Doc](https://yevgendemotestorganization.github.io/bring-core-javadoc/com/bobocode/bring/core/exception/NoUniqueBeanException.html)