# Web Configuration Guide

This guide provides instructions on how to configure *Bring* application properties. 
The properties mentioned here control the server port, context path and whether stack traces should be displayed.

## Configuring `server.port`

The `server.port` property determines the port on which your application will run. 
Default port is `9000`. To set it to another one (e.g. `9090`), add the following line to your `application.properties` file:

```properties
server.port=9090
```

## Configuring `server.contextPath`
The server.contextPath property specifies the context path under which your application will be accessible. 
If you want the root context, leave it empty. To set a specific context path (e.g. `/app`), add the following line to your `application.properties` file:

```properties
server.contextPath=/app
```

## Configuring `server.withStackTrace`
The server.withStackTrace property controls whether stack traces are displayed in the response during an error. 
The default value is `false`. To include stack traces add the following line to your `application.properties` file:

```properties
server.withStackTrace=true
```

### See Also
- [REST API](../servlet/RestApi.md)
- [Web Module](../../Web.md)
