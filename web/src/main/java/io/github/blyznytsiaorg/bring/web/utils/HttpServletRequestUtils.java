package io.github.blyznytsiaorg.bring.web.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

/**
 * The {@code HttpServletRequestUtils} class provides utility methods for working with {@link HttpServletRequest}.
 * It includes methods to retrieve the request path and obtain a shortened version of the request path.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@UtilityClass
public class HttpServletRequestUtils {

    /**
     * An empty string constant.
     */
    public static final String EMPTY = "";

    /**
     * Retrieves the request path from the given {@link HttpServletRequest}, excluding the context path.
     *
     * @param req The HttpServletRequest from which to extract the request path.
     * @return The request path without the context path.
     */
    public static String getRequestPath(HttpServletRequest req) {
        String contextPath = req.getContextPath();
        String requestURI = req.getRequestURI();
        if (requestURI.startsWith(contextPath)) {
            requestURI = requestURI.replace(contextPath, EMPTY);
        }
        return requestURI.equals("/") ? "" : requestURI;
    }

    /**
     * Obtains a shortened version of the given request path, excluding the last segment (e.g., file or resource name).
     *
     * @param requestPath The request path to be shortened.
     * @return The shortened request path.
     */
    public static String getShortenedPath(String requestPath) {
        String requestPathShortened;
        int index = requestPath.lastIndexOf("/");
        requestPathShortened = requestPath.substring(0, index + 1);
        return requestPathShortened;
    }
}
