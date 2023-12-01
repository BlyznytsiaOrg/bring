# ServerProperties

The `ServerProperties` class serves as a configuration container for various settings related to the web server. These settings are typically defined in configuration files and are injected into the class using the `@Value` annotation. Instances of this class are utilized to centralize and manage configuration settings for the web server within the application.

### Properties:
 - port:
   - Description: The port on which the web server will listen for incoming requests.
   - Default Value: 9000
   - Configuration: The value is obtained from the property `server.port` with a default value of 9000.
 - contextPath:
   - Description: The context path for the web server, representing the base path for all requests handled by the server.
   - Default Value: Empty String (no context path)
   - Configuration: The value is obtained from the property `server.contextPath`.
 - withStackTrace:
   - Description: Indicates whether stack traces should be included in error responses.
   - Default Value: false
   - Configuration: The value is obtained from the property `server.withStackTrace` with a default value of false.
 - withMessage:
   - Description: Configuration property for controlling whether commit messages are included in GitInfo responses.
   - Default Value: false
   - Configuration: The value is obtained from the property `server.actuator.git.commit.withMessage` with a default value of false. It is used by the DefaultActuatorService to determine whether to fetch and display commit messages.
   
### Configuration Example:
 ```
   server.port = 9090
   server.contextPath =
   server.withStackTrace = false
   server.actuator.git.commit.withMessage = false
 ```
- In this example configuration:
  - The web server will listen on port 9090.
  - There is no specific context path defined.
  - Stack traces will not be included in error responses.
  - Commit messages will not be included in GitInfo responses.
  - These properties allow for flexible configuration of the web server behavior within the application.
