# Bring Web

## General Web flow diagram

![Bring DI diagram](https://github.com/YevgenDemoTestOrganization/bring/assets/73576438/f00bb773-268d-440c-9603-9a8894210896)

1. The BringWebApplication offers a straightforward way to initialize and operate a BringWebApplication. Users can establish and customize a BringWebApplication using a core BringApplicationContext delegate, allowing for the return of a context reference.
2. The BringApplicationContext packs essential data for BringWebApplication, enabling its storage within a web context for later use.
3. Once all beans are configured, the transition to the Web Server builder becomes possible.
4. Bringing BringApplicationContext into play involves registering it as an attribute within the servletContext for the Web Server.
5. The Web Server is equipped with a registered API general error handler.
6. The registration process involves preparing the Dispatcher servlet for the Web Server.
7. Gathering all ParamResolver components.
8. Gathering all BringServlets.
9. With the Dispatcher servlet aggregating necessary information, it's primed for registration.
10. Initiating the server boot process.

## More general simplified flow:

- Bring Web app is executed
- Bring context is initialized
- Servlet container configuration.
- Servlet web server is started.
- DispatcherServlet is created and registered.

## Documentations

- We have two types of it via Markdown (see below) and [JavaDoc](https://yevgendemotestorganization.github.io/bring-web-javadoc/)

## Features:

- Embedded Servers: These are servers integrated or bundled within an application or system. They allow the application to run without needing an external server. These servers are often lightweight and designed for specific purposes or frameworks.
- Dispatcher Servlet: In the context of Java-based web applications, a Dispatcher Servlet is the front controller in the Bring framework's MVC architecture. It receives incoming requests and directs them to the appropriate resources (controllers, views, etc.) for processing.
- REST API: Representational State Transfer (REST) API is a set of rules and conventions for building web services that enable communication between different systems on the internet. It uses HTTP requests to perform CRUD (Create, Read, Update, Delete) operations on resources and typically returns responses in JSON or XML (//TODO ) format.
- [Static Content Serving](web/servlet/StaticResourceController.md): This refers to the process of serving static files (HTML, CSS, JavaScript, images, etc.) directly to clients without any processing or modification by the server. This content is typically served as-is from the server's file system or a specified directory.
- [Exception handler](web/servlet/JsonExceptionHandler.md): This component specializes in managing exceptions by intercepting and customizing error responses in JSON format. It extends ErrorReportValve to provide tailored error handling within a Servlet container.


- addition items:
  - The code supports the generation of a random port based on the key ${random.port}. When this key is encountered as the value for a property, it is replaced with a randomly generated port number within the specified range. The range for the random port generation is defined by the constants START_PORT_RANGE and END_PORT_RANGE, which are set to 8000 and 9000, respectively. This feature allows dynamic allocation of ports, which can be useful in scenarios where a unique port is needed, and the actual port number is not predetermined but instead generated at runtime.

- annotation: 
  - [@ResponseStatus](web/servlet/annotation/ResponseStatus.md)

Exception scenarios:

- ConnectorStartFailedException
- WebServerException
- General error handling for API
- MethodArgumentTypeMismatchException
- MissingServletRequestParameterException
- TypeArgumentUnsupportedException
