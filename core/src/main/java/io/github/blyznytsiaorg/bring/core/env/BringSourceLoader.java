package io.github.blyznytsiaorg.bring.core.env;

import java.util.Map;

/**
 * Interface for a Bring source loader, defining methods to retrieve file extensions and load resources.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */

public interface BringSourceLoader {

    /**
     * Retrieves the file extensions supported by this source loader.
     *
     * @return A string representing the file extensions supported by this loader.
     */
    String getFileExtensions();

    /**
     * Loads a resource by the given name.
     *
     * @param name The name of the resource to be loaded.
     * @return A map containing data loaded from the specified resource.
     */
    Map<String, String> load(String name);
}
