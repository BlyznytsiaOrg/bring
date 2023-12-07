package io.github.blyznytsiaorg.bring.web.servlet.http;

import lombok.experimental.UtilityClass;

/**
 * Utility class for representing common media types used in HTTP requests and responses.
 * Provides constants for commonly used media type values.
 *
 * <p>This class is marked as a utility class with the {@link lombok.experimental.UtilityClass} annotation,
 * indicating that it contains only static methods or fields and should not be instantiated.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@UtilityClass
public class MediaType {

    /**
     * Represents the "application/json" media type.
     */
    public static final String APPLICATION_JSON_VALUE = "application/json";
    /**
     * Represents the "text/html" media type.
     */
    public static final String TEXT_HTML_VALUE = "text/html";
    /**
     * Represents the "UTF-8" character encoding.
     */
    public static final String UTF8_VALUE = "UTF-8";

}
