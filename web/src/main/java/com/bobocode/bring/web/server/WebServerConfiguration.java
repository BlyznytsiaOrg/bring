package com.bobocode.bring.web.server;

import com.bobocode.bring.core.annotation.Autowired;
import com.bobocode.bring.core.annotation.Bean;
import com.bobocode.bring.core.annotation.Configuration;
import com.bobocode.bring.web.server.properties.ServerProperties;
import com.bobocode.bring.web.servlet.error.ErrorResponseCreator;
import com.bobocode.bring.web.servlet.JsonExceptionHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Configuration class for setting up and customizing the web server and related beans.
 *
 * <p>
 * The {@code WebServerConfiguration} class is responsible for defining beans related
 * to the embedded web server, object mapping, error handling.
 * </p>
 *
 * @see Configuration
 * @see Bean
 * @see ServerProperties
 * @see TomcatServletWebServerFactory
 * @see ErrorResponseCreator
 * @see JsonExceptionHandler
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Configuration
public class WebServerConfiguration {

    @Autowired
    private ServerProperties serverProperties;

    /**
     * Creates and returns a {@link TomcatServletWebServerFactory} bean for configuring
     * the embedded Tomcat web server. The port and context path are obtained from the
     * {@link ServerProperties} bean.
     *
     * @return a configured instance of {@link TomcatServletWebServerFactory}
     * @see TomcatServletWebServerFactory
     */
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory(serverProperties.getPort(), serverProperties.getContextPath());
    }

    /**
     * Creates and returns an {@link ObjectMapper} bean for JSON object mapping.
     *
     * @return a configured instance of {@link ObjectMapper}
     */
    @Bean
    public ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }

    /**
     * Creates and returns an {@link ErrorResponseCreator} bean for preparing error
     * responses.
     *
     * @return a new instance of {@link ErrorResponseCreator}
     * @see ErrorResponseCreator
     */
    @Bean
    public ErrorResponseCreator prepareErrorResponseCreator() {
        return new ErrorResponseCreator();
    }

    /**
     * Creates and returns a {@link JsonExceptionHandler} bean for handling JSON
     * exceptions. This bean is configured with an {@link ObjectMapper} and
     * {@link ServerProperties}.
     *
     * @return a new instance of {@link JsonExceptionHandler}
     * @see JsonExceptionHandler
     */
    @Bean
    public JsonExceptionHandler jsonExceptionHandler() {
        return new JsonExceptionHandler(
                objectMapper(),
                prepareErrorResponseCreator(),
                serverProperties);
    }
}
