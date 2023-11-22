package com.bobocode.bring.web.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpServletRequestUtils {

    public static final String EMPTY = "";

    public static String getRequestPath(HttpServletRequest req) {
        String contextPath = req.getContextPath();
        String requestURI = req.getRequestURI();
        if (requestURI.startsWith(contextPath)) {
            requestURI = requestURI.replace(contextPath, EMPTY);
        }
        return requestURI;
    }
}
