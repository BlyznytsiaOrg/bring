# MissingRequestHeaderAnnotationValueException

The `MissingRequestHeaderAnnotationValueException` class is a runtime exception that indicates the absence of a required value for the `@RequestHeader` annotation in a method parameter.

## Description
This exception is thrown when a required value is missing for the `@RequestHeader` annotation in a method parameter.

### Constructor
```
public MissingRequestHeaderAnnotationValueException(String message)
```

### See Also

- [`@RequestHeader`](features/web/servlet/annotation/RequestHeader.md)