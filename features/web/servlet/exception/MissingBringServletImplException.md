# MissingBringServletImplException

The `MissingBringServletImplException` class is a runtime exception that indicates the absence of the implementation of the `BringServlet` interface in a `RestController`.

## Description
This exception is thrown when a `BringServlet` interface implementation is missing in a class annotated by `@RestController`.

### Constructor
```
public MissingBringServletImplException(String message)
```

### See Also

- [`BringServlet`](todo: insert link)
- [`@RestController`](features/web/servlet/annotation/RestController.md)