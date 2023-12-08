# ConnectorStartFailedException Documentation

## Overview

Exception thrown when a connector configured to listen on a specific port fails to start.

Instances of this exception are typically thrown when an embedded web server, such as Apache Tomcat, encounters an issue while attempting to start a connector on a specified port.

## Inheritance Hierarchy

- Inherits from: `WebServerException`

## Constructor

```java
public ConnectorStartFailedException(int port)
```

- [Java Doc](https://BlyznytsiaOrg.github.io/bring-web-javadoc/com/bobocode/bring/web/server/exception/ConnectorStartFailedException.html)
