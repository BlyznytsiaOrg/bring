# BringServlet Interface Documentation

The `BringServlet` interface is a marker interface used to identify classes that need to be implemented for use as REST controllers within the Bring framework. Classes implementing this interface play a significant role in the configuration and setup of the REST controller context.

## Overview

The primary purpose of the `BringServlet` interface is to act as a marker, signaling to the system that a particular class should be recognized as a servlet within the Bring framework. It does not mandate the addition of any specific methods, serving solely as an indicator for classes that should be included in the REST controller context setup.

## Usage

To make use of the `BringServlet` interface, a class needs to implement it:

```java
@RestController
public class MyRestController implements BringServlet {
    // REST controller logic and methods
}
```