# MissingRequestParamException

The `MissingRequestParamException` class is a runtime exception that indicates the absence of a required request parameter for a method parameter type annotated with `@RequestParam`.

## Description
This exception is thrown when a required request parameter is missing for a method parameter type annotated with `@RequestParam`.

### Constructor
```
public MissingRequestParamException(String message)
```

### See Also

- [`@RequestParam`](../annotation/RequestParam.md)