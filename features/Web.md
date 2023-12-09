[![Maven Central](https://img.shields.io/maven-central/v/io.github.blyznytsiaorg.bring.web/web.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.blyznytsiaorg.bring.web/web)

# Bring Web

## General Web Flow Diagram

![Bring DI diagram](https://github.com/BlyznytsiaOrg/bring/assets/114337016/06604708-d660-4e2c-ba08-ef8b215bbcb3)

1. The **BringWebApplication** offers a straightforward way to initialize and operate. Users can
   establish and customize it using a core **BringApplicationContext** delegate, allowing the return
   of a context reference.
2. The **BringApplicationContext** packs essential data for **BringWebApplication**, enabling its
   storage within a web context for later use.
3. Once all beans are configured, the **RestControllerContext** is created, representing the context
   for managing REST controllers and their associated parameters in a web application.
4. Gathering all **BringServlets**.
5. Gathering all **ParamResolver** components.
6. The **WebStarter** initializes and runs a web application.
7. Starting **Apache Tomcat** as the embedded servlet container.
8. Bringing **BringApplicationContext** into play involves registering it as an attribute within the
   **ServletContext**.
9. Registering **RestControllerContext** as an attribute within the **ServletContext**.
10. The Web Server is equipped with a registered **API General Error Handler**.
11. The registration process involves preparing and registering the **Dispatcher Servlet** for the
    Web Server.

## More general simplified flow:

- Bring Web app is executed
- Bring context is initialized
- Servlet container configuration.
- RestController context is created.
- Servlet web server is started.
- DispatcherServlet is created and registered.

## Documentations

- There are two types of documentation: Markdown (see below) and [JavaDoc](https://blyznytsiaOrg.github.io/bring-web-javadoc/)

## Features:

- [Embedded Server](web/server/TomcatWebServer.md): This is server integrated or bundled within an application or system. It allows the application to run without needing an external server. The server is often lightweight and designed for specific purposes or frameworks.
- [Dispatcher Servlet](web/servlet/DispatcherServlet.md): In the context of Java-based web applications, a Dispatcher Servlet is the front controller in the Bring framework's MVC architecture. It receives incoming requests and directs them to the appropriate resources (controllers, views, etc.) for processing.
- [REST API](web/servlet/RestApi.md): Representational State Transfer (REST) API is a set of rules and conventions for building web services that enable communication between different systems on the internet. It uses HTTP requests to perform CRUD (Create, Read, Update, Delete) operations on resources and typically returns responses in JSON format.
- [Static Content Serving](web/servlet/StaticResourceController.md): This refers to the process of serving static files (HTML, CSS, JavaScript, images, etc.) directly to clients without any processing or modification by the server. This content is typically served as-is from the server's file system or a specified directory.
- [Exception handler](web/servlet/JsonExceptionHandler.md): This component specializes in managing exceptions by intercepting and customizing error responses in JSON format. It extends ErrorReportValve to provide tailored error handling within a Servlet container.
- [Actuator](web/servlet/Actuator.md): Provides endpoints for checking application health, retrieving environment properties, Git information, and dynamically changing log levels.

[**Web Configuration Guide**](web/server/ConfigGuide.md)

- addition items:
  - The code supports the generation of a random port based on the key ${random.port}. When this key is encountered as the value for a property, it is replaced with a randomly generated port number within the specified range. The range for the random port generation is defined by the constants START_PORT_RANGE and END_PORT_RANGE, which are set to 8000 and 9000, respectively. This feature allows dynamic allocation of ports, which can be useful in scenarios where a unique port is needed, and the actual port number is not predetermined but instead generated at runtime.
  - The `ValuePropertiesPostProcessor` class supports dynamic profile configuration using VM parameters. Set the active profile with the `-Dbring.profiles.active=dev` parameter during application launch. This enables the class to resolve profile-specific properties and apply them to the `DefaultBringBeanFactory`, allowing for adaptable configuration based on the specified profile. Additionally, the active profiles influence the loading of different `application.properties` files. For example, with an active profile of "dev," the class will load properties from `application-dev.properties`. This mechanism allows for customized property sets tailored to specific application environments.
  - The `ServerProperties` class serves as a configuration container for various settings related to the web server. These settings are typically defined in configuration files and are injected into the class using the `@Value` annotation. Instances of this class are utilized to centralize and manage configuration settings for the web server within the application. See more: [ServerProperties](web/servlet/ServerProperties.md)


- annotations:
    - [@GetMapping](web/servlet/annotation/GetMapping.md)
    - [@Controller](web/servlet/annotation/Controller.md)
    - [@RestController](web/servlet/annotation/RestController.md)
    - [@PostMapping](web/servlet/annotation/PostMapping.md)
    - [@PutMapping](web/servlet/annotation/PutMapping.md)
    - [@PatchMapping](web/servlet/annotation/PatchMapping.md)
    - [@DeleteMapping](web/servlet/annotation/DeleteMapping.md)
    - [@PathVariable](web/servlet/annotation/PathVariable.md)
    - [@RequestParam](web/servlet/annotation/RequestParam.md)
    - [@RequestBody](web/servlet/annotation/RequestBody.md)
    - [@RequestHeader](web/servlet/annotation/RequestHeader.md)
    - [@ResponseStatus](web/servlet/annotation/ResponseStatus.md)

Exception scenarios:

- [ConnectorStartFailedException](web/server/exception/ConnectorStartFailedException.md)
- [WebServerException](web/server/exception/WebServerException.md)
- [MethodArgumentTypeMismatchException](web/servlet/exception/MethodArgumentTypeMismatchException.md)
- [MissingApplicationMappingException](web/servlet/exception/MissingApplicationMappingException.md)
- [MissingBringServletImplException](web/servlet/exception/MissingBringServletImplException.md)
- [MissingRequestHeaderAnnotationValueException](web/servlet/exception/MissingRequestHeaderAnnotationValueException.md)
- [MissingRequestParamException](web/servlet/exception/MissingRequestParamException.md)
- [RequestBodyTypeUnsupportedException](web/servlet/exception/RequestBodyTypeUnsupportedException.md)
- [RequestPathDuplicateException](web/servlet/exception/RequestPathDuplicateException.md)
- [TypeArgumentUnsupportedException](web/servlet/exception/TypeArgumentUnsupportedException.md)

