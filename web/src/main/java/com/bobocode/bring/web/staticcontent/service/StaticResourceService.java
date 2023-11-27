package com.bobocode.bring.web.staticcontent.service;

import java.nio.file.Path;

/**
 * Interface defining methods for handling static resource requests.
 * Implementations of this interface are responsible for retrieving the absolute path of requested static files.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public interface StaticResourceService {

    /**
     * Retrieves the absolute path for the requested static file.
     *
     * @param pathToFile The path of the requested static file.
     * @return The absolute path of the requested static file.
     */
    Path getStaticFile(String pathToFile);

}
