# MissingBringServletImplException

The `MissingBringServletImplException` class is a runtime exception that indicates the absence of the implementation of the `BringServlet` interface in a `RestController`.

## Description
This exception is thrown when a `BringServlet` interface implementation is missing in a class annotated by `@RestController`.

### Constructor
```
public MissingBringServletImplException(String message)
```

- [Java Doc](https://BlyznytsiaOrg.github.io/bring-web-javadoc/com/bobocode/bring/web/servlet/exception/MissingBringServletImplException.html)

### See Also

- [BringServlet](../BringServlet.md)
- [@RestController](../annotation/RestController.md)