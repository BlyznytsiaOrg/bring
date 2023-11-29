package com.bobocode.bring.web.staticcontent.controller;

import com.bobocode.bring.web.servlet.BringServlet;
import com.bobocode.bring.web.servlet.annotation.Controller;
import com.bobocode.bring.web.servlet.annotation.GetMapping;
import com.bobocode.bring.web.staticcontent.service.StaticResourceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Controller responsible for handling static resource requests.
 * Provides an endpoint to retrieve static files based on the requested URI.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Controller
@RequiredArgsConstructor
public class StaticResourceController implements BringServlet {

    // Service for retrieving static resources
    private final StaticResourceService staticResourceService;

    /**
     * Handles GET requests for static resources.
     *
     * @param request  The incoming HTTP request.
     * @param response The outgoing HTTP response.
     */
    @GetMapping(path = "/static/*")
    @SneakyThrows
    public void getStaticFile(HttpServletRequest request, HttpServletResponse response) {
        Path resourceAbsolutePath = staticResourceService.getStaticFile(request.getRequestURI());

        setContentType(response, resourceAbsolutePath);
        Files.copy(resourceAbsolutePath, response.getOutputStream());
    }

    /**
     * Sets the content type in the HTTP response based on the file type.
     *
     * @param response The outgoing HTTP response.
     * @param path     The path of the static file.
     * @throws IOException If an I/O error occurs while processing the response.
     */
    private void setContentType(HttpServletResponse response, Path path) throws IOException {
        String contentType = Files.probeContentType(path);
        if (contentType != null) {
            response.setContentType(contentType);
        }
    }
}
