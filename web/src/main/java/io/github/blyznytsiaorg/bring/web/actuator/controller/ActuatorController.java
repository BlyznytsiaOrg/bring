package io.github.blyznytsiaorg.bring.web.actuator.controller;

import io.github.blyznytsiaorg.bring.web.actuator.dto.GitInfo;
import io.github.blyznytsiaorg.bring.web.actuator.dto.PropertiesData;
import io.github.blyznytsiaorg.bring.web.actuator.service.impl.DefaultActuatorService;
import io.github.blyznytsiaorg.bring.web.actuator.utils.LogLevelChangerUtils;
import io.github.blyznytsiaorg.bring.web.servlet.BringServlet;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.GetMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.PostMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RequestMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RequestParam;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.ResponseStatus;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RestController;
import io.github.blyznytsiaorg.bring.web.servlet.http.HttpStatus;
import lombok.RequiredArgsConstructor;

/**
 * Controller responsible for managing actuator endpoints.
 * Provides endpoints for checking application health, retrieving environment properties, Git information,
 * and dynamically changing log levels.
 * <p>
 * The class is annotated with {@code @RestController}, indicating that it handles HTTP requests and
 * returns the response in the specified format. The base path for all endpoints is "/actuator".
 * <p>
 * The class has endpoints for checking the health of the application, retrieving environment properties,
 * and getting Git information. Additionally, it includes an endpoint for dynamically changing the log level
 * of specific loggers based on the package name.
 * <p>
 * Uses the {@code DefaultActuatorService} for preparing actuator-related data and the
 * {@code LogLevelChangerUtils} for dynamically changing log levels.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/actuator")
public class ActuatorController implements BringServlet {

    // Service for preparing actuator-related data
    private final DefaultActuatorService defaultActuatorService;

    /**
     * Endpoint for checking the health of the application.
     *
     * @return The HTTP status representing the health of the application.
     */
    @GetMapping(path = "/health")
    @ResponseStatus(value = HttpStatus.OK)
    public String health() {
        return HttpStatus.OK.name();
    }

    /**
     * Endpoint for retrieving environment properties of the application.
     *
     * @return The environment properties of the application.
     */
    @GetMapping(path = "/env")
    @ResponseStatus(value = HttpStatus.OK)
    public PropertiesData env() {
        return defaultActuatorService.preparePropertyData();
    }

    /**
     * Endpoint for retrieving Git information about the application.
     *
     * @return The Git information of the application.
     */
    @GetMapping(path = "/info")
    @ResponseStatus(value = HttpStatus.OK)
    public GitInfo info() {
        return defaultActuatorService.getGitInfo();
    }

    /**
     * Endpoint for dynamically changing the log level of specific loggers based on package name.
     *
     * @param packageName The name of the logger's package to change the level.
     * @param newLevel    The new log level for the specified logger's package.
     */
    @PostMapping(path = "/loggers")
    public void logger(@RequestParam String packageName, @RequestParam String newLevel) {
        LogLevelChangerUtils.changeLogLevel(packageName, newLevel);
    }
}
