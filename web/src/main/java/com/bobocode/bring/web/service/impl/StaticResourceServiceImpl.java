package com.bobocode.bring.web.service.impl;

import com.bobocode.bring.core.anotation.Service;
import com.bobocode.bring.web.server.properties.ServerProperties;
import com.bobocode.bring.web.service.StaticResourceService;
import com.bobocode.bring.web.servlet.exception.StaticFileNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Implementation of the {@link StaticResourceService} interface for handling static resource requests.
 * Manages the retrieval of absolute paths for requested static files, ensuring file existence and proper conditions.
 *
 * @author Blyzhnytsia Team
 * @version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StaticResourceServiceImpl implements StaticResourceService {

    // Constants for error messages
    private static final String STATIC_FILE_NOT_FOUND_MESSAGE = "Can't find the File: %s.";
    private static final String STATIC_FOLDER_NOT_FOUND_MESSAGE = "Static folder can't be Null";

    // Injected server properties for static folder information
    private final ServerProperties serverProperties;

    /**
     * Retrieves the absolute path for the requested static file.
     *
     * @param pathToFile The path of the requested static file.
     * @return The absolute path of the requested static file.
     * @throws StaticFileNotFoundException If the file is not found or doesn't meet specified conditions.
     */
    @Override
    @SneakyThrows
    public Path getStaticFile(String pathToFile) {
        URL folderUrl = StaticResourceServiceImpl.class.getClassLoader()
                .getResource(serverProperties.getStaticFolder());
        Objects.requireNonNull(folderUrl, STATIC_FOLDER_NOT_FOUND_MESSAGE);

        Path folderPath = Paths.get(folderUrl.toURI()).resolve("..").normalize();

        Path resourceAbsolutePath = Paths.get(folderPath.toString(), pathToFile);
        checkPathFile(resourceAbsolutePath, pathToFile);

        return resourceAbsolutePath;
    }

    /**
     * Checks if the file exists, is not a directory, and is within the specified static folder.
     * If conditions are not met, logs an error and throws a {@link StaticFileNotFoundException}.
     *
     * @param path       The path of the file to be checked.
     * @param requestUri The original request URI for logging purposes.
     * @throws StaticFileNotFoundException If the file is not found or doesn't meet specified conditions.
     */
    private void checkPathFile(Path path, String requestUri) {
        if (!Files.exists(path) || Files.isDirectory(path)
                || !path.toString().contains(serverProperties.getStaticFolder())) {

            log.error(String.format(STATIC_FILE_NOT_FOUND_MESSAGE, requestUri));
            throw new StaticFileNotFoundException(String.format(STATIC_FILE_NOT_FOUND_MESSAGE, requestUri));
        }
    }
}
