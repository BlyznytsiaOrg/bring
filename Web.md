# Bring Web

## General Web flow diagram

![Bring DI diagram](https://github.com/YevgenDemoTestOrganization/bring/assets/73576438/863a2385-0848-42b2-934b-783088807bc9)

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

## Features:

- Embedded Servers: These are servers integrated or bundled within an application or system. They allow the application to run without needing an external server. These servers are often lightweight and designed for specific purposes or frameworks.
- Dispatcher Servlet: In the context of Java-based web applications, a Dispatcher Servlet is the front controller in the Bring framework's MVC architecture. It receives incoming requests and directs them to the appropriate resources (controllers, views, etc.) for processing.
- REST API: Representational State Transfer (REST) API is a set of rules and conventions for building web services that enable communication between different systems on the internet. It uses HTTP requests to perform CRUD (Create, Read, Update, Delete) operations on resources and typically returns responses in JSON or XML (//TODO ) format.
- Static Content Serving: This refers to the process of serving static files (HTML, CSS, JavaScript, images, etc.) directly to clients without any processing or modification by the server. This content is typically served as-is from the server's file system or a specified directory.


Exception scenarios:

- ConnectorStartFailedException
- WebServerException
- General error handling for API
- MethodArgumentTypeMismatchException
- MissingServletRequestParameterException
- TypeArgumentUnsupportedException
