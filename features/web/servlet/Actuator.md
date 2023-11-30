# ActuatorController

Controller responsible for managing actuator endpoints. Provides endpoints for checking application health, retrieving environment properties, Git information, and dynamically changing log levels.

The class is annotated with `@RestController`, indicating that it handles HTTP requests and returns the response in the specified format. The base path for all endpoints is "/actuator".

## Endpoints

- **Health Check Endpoint:**
    - URL: `/actuator/health`
    - Method: `GET`
    - Response Status: `HttpStatus.OK`
    - Returns: The HTTP status representing the health of the application.

- **Environment Properties Endpoint:**
    - URL: `/actuator/env`
    - Method: `GET`
    - Response Status: `HttpStatus.OK`
    - Returns: The environment properties of the application.

- **Git Information Endpoint:**
    - URL: `/actuator/info`
    - Method: `GET`
    - Response Status: `HttpStatus.OK`
    - Returns: The Git information of the application.

- **Logger Level Change Endpoint:**
    - URL: `/actuator/loggers`
    - Method: `POST`
    - Parameters:
        - `packageName`: The name of the logger's package to change the level.
        - `newLevel`: The new log level for the specified logger's package.
    - Effect: Dynamically changes the log level of specific loggers based on the package name.

## Dependencies

Uses the `DefaultActuatorService` for preparing actuator-related data and the `LogLevelChangerUtils` for dynamically changing log levels.

## Usage

```java
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/actuator")
public class ActuatorController implements BringServlet {

    // Service for preparing actuator-related data
    private final DefaultActuatorService defaultActuatorService;

    // ... (Endpoint methods and other members)

    // Example: Health Check Endpoint
    @GetMapping(path = "/health")
    @ResponseStatus(value = HttpStatus.OK)
    public String health() {
        return HttpStatus.OK.name();
    }

    // ... (Other endpoint methods)

    // Example: Logger Level Change Endpoint
    @PostMapping(path = "/loggers")
    public void logger(@RequestParam String packageName, @RequestParam String newLevel) {
        LogLevelChangerUtils.changeLogLevel(packageName, newLevel);
    }
}
```


The `ActuatorController` provides essential insights into the application's status and configuration, making it a valuable component for monitoring and management.
