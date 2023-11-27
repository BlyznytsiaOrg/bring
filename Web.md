# Bring Web

## General Web flow diagram

![Bring DI diagram](https://github.com/YevgenDemoTestOrganization/bring/assets/114337016/06604708-d660-4e2c-ba08-ef8b215bbcb3)

1. The **BringWebApplication** offers a straightforward way to initialize and operate. Users can establish and customize it using a core **BringApplicationContext** delegate, allowing the return of a context reference.
2. The **BringApplicationContext** packs essential data for **BringWebApplication**, enabling its storage within a web context for later use.
3. Once all beans are configured, the **RestControllerContext** is created, representing the context for managing REST controllers and their associated parameters in a web application.
4. Gathering all **BringServlets**.
5. Gathering all **ParamResolver** components.
6. The **WebStarter** initializes and runs a web application.
7. Starting **Apache Tomcat** as the embedded servlet container.
8. Bringing **BringApplicationContext** into play involves registering it as an attribute within the **ServletContext**.
9. Registering **RestControllerContext** as an attribute within the **ServletContext**.
10. The Web Server is equipped with a registered **API General Error Handler**. 
11. The registration process involves preparing and registering the **Dispatcher Servlet** for the Web Server.

## More general simplified flow:

- Bring Web app is executed
- Bring context is initialized
- Servlet container configuration.
- RestController context is created.
- Servlet web server is started.
- DispatcherServlet is created and registered.

## Features:

- Embedded Servers: These are servers integrated or bundled within an application or system. They allow the application to run without needing an external server. These servers are often lightweight and designed for specific purposes or frameworks.
- Dispatcher Servlet: In the context of Java-based web applications, a Dispatcher Servlet is the front controller in the Bring framework's MVC architecture. It receives incoming requests and directs them to the appropriate resources (controllers, views, etc.) for processing.
- REST API: Representational State Transfer (REST) API is a set of rules and conventions for building web services that enable communication between different systems on the internet. It uses HTTP requests to perform CRUD (Create, Read, Update, Delete) operations on resources and typically returns responses in JSON or XML (//TODO ) format.
- [Static Content Serving](features/web/servlet/StaticResourceController.md): This refers to the process of serving static files (HTML, CSS, JavaScript, images, etc.) directly to clients without any processing or modification by the server. This content is typically served as-is from the server's file system or a specified directory.
- [Exception handler](/features/web/servlet/JsonExceptionHandler.md): This component specializes in managing exceptions by intercepting and customizing error responses in JSON format. It extends ErrorReportValve to provide tailored error handling within a Servlet container.


- addition items:
  - The code supports the generation of a random port based on the key ${random.port}. When this key is encountered as the value for a property, it is replaced with a randomly generated port number within the specified range. The range for the random port generation is defined by the constants START_PORT_RANGE and END_PORT_RANGE, which are set to 8000 and 9000, respectively. This feature allows dynamic allocation of ports, which can be useful in scenarios where a unique port is needed, and the actual port number is not predetermined but instead generated at runtime.

- annotation: 
  - [@ResponseStatus](features/web/servlet/annotation/ResponseStatus.md)

Exception scenarios:

- [ConnectorStartFailedException](features/web/server/exception/ConnectorStartFailedException.md)
- [WebServerException](features/web/server/exception/WebServerException.md)
- General error handling for API
- [MethodArgumentTypeMismatchException](features/web/servlet/exception/MethodArgumentTypeMismatchException.md)
- [MissingApplicationMappingException](features/web/servlet/exception/MissingApplicationMappingException.md)
- [MissingBringServletImplException](features/web/servlet/exception/MissingBringServletImplException.md)
- [MissingRequestHeaderAnnotationValueException](features/web/servlet/exception/MissingRequestHeaderAnnotationValueException.md)
- [MissingRequestParamException](features/web/servlet/exception/MissingRequestParamException.md)
- [RequestBodyTypeUnsupportedException](features/web/servlet/exception/RequestBodyTypeUnsupportedException.md)
- [RequestPathDuplicateException](features/web/servlet/exception/RequestPathDuplicateException.md)
- [TypeArgumentUnsupportedException](features/web/servlet/exception/TypeArgumentUnsupportedException.md)
