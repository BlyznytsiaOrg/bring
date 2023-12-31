package io.github.blyznytsiaorg.bring.web.staticcontent.service.impl;

import io.github.blyznytsiaorg.bring.core.annotation.Service;
import io.github.blyznytsiaorg.bring.web.staticcontent.exception.StaticFileNotFoundException;
import io.github.blyznytsiaorg.bring.web.staticcontent.service.StaticResourceService;
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
 * @since 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultStaticResourceService implements StaticResourceService {

    // Constants for error messages
    private static final String STATIC_FILE_NOT_FOUND_MESSAGE = "Can't find the File: %s.";
    private static final String STATIC_FOLDER_NOT_FOUND_MESSAGE = "Static folder can't be Null";
    private static final String STATIC_FOLDER = "static";

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
        URL folderUrl = DefaultStaticResourceService.class.getClassLoader().getResource(STATIC_FOLDER);
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
        if (!Files.exists(path) || Files.isDirectory(path) || !path.toString().contains(STATIC_FOLDER)) {

            log.warn(String.format(STATIC_FILE_NOT_FOUND_MESSAGE, requestUri));
            throw new StaticFileNotFoundException(String.format(STATIC_FILE_NOT_FOUND_MESSAGE, requestUri),
                    null, true, false);
        }
    }
}
