## CyclicBeanException


### Example Scenario

In this example, we'll demonstrate how cycle dependencies can emerge within a DI framework using beans and constructor injection.


Components Involved:
- Bean A

Requires an instance of Bean C for its functionality.

- Bean C

Relies on Bean A to perform certain operations.

Code Snippet:

```
@Component
public class BeanA {
    private final BeanB beanB;

    public BeanA(BeanB beanB) {
        this.beanB = beanB;
    }
}

@Component
public class BeanB {
    private final BeanA beanA;

    public BeanB(BeanA beanA) {
        this.beanA = beanA;
    }
}

```

### Explanation:
**Issue Identification:** 

    BeanA requires BeanB, and BeanB requires BeanA, forming a cyclic dependency.

**Impact:** 

    This dependency cycle can lead to initialization problems or runtime errors when the framework attempts to create instances of these beans.

Using setter or field injection can be an alternative approach to address cycle dependencies within a Bring DI framework. 
However, careful management is crucial to avoid reintroducing cyclic dependencies through setter or field methods, maintaining the framework's integrity and preventing runtime issues.

[Java Doc](https://BlyznytsiaOrg.github.io/bring-core-javadoc/com/bobocode/bring/core/exception/CyclicBeanException.html)