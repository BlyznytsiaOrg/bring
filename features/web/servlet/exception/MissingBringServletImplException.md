# MissingBringServletImplException

The `MissingBringServletImplException` class is a runtime exception that indicates the absence of the implementation of the `BringServlet` interface in a `RestController`.

## Description
This exception is thrown when a `BringServlet` interface implementation is missing in a class annotated by `@RestController`.

### Constructor
```
public MissingBringServletImplException(String message)
```

### See Also

- [BringServlet](https://github.com/YevgenDemoTestOrganization/bring/blob/d1df5bd13e15033caad3f012bc3ef5c3be780c1f/features/web/servlet/BringServlet.md)
- [@RestController](https://github.com/YevgenDemoTestOrganization/bring/blob/09aafc6d471c5f793eea58cf8793c68443ec14e8/features/web/servlet/annotation/RestController.md)