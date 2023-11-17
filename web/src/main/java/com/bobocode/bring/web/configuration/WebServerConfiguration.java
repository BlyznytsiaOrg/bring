package com.bobocode.bring.web.configuration;

import com.bobocode.bring.web.embeddedTomcat.TomcatServletWebServerFactory;
import com.bobocode.bring.web.service.ErrorResponseCreator;
import com.bobocode.bring.web.servlet.DispatcherServlet;
import com.bobocode.bring.web.servlet.JsonExceptionHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// @Configuration
public class WebServerConfiguration {

    //    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory(9090, "/app");
    }

    //    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    public ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }

    public ErrorResponseCreator prepareErrorResponseCreator() {
        return new ErrorResponseCreator();
    }

    public JsonExceptionHandler jsonExceptionHandler() {
        return new JsonExceptionHandler(
                objectMapper(),
                prepareErrorResponseCreator(),
                false);
    }
}
