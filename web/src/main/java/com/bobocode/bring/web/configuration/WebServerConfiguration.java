package com.bobocode.bring.web.configuration;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Bean;
import com.bobocode.bring.core.anotation.Configuration;
import com.bobocode.bring.web.embeddedTomcat.TomcatServletWebServerFactory;
import com.bobocode.bring.web.service.ErrorResponseCreator;
import com.bobocode.bring.web.servlet.JsonExceptionHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class WebServerConfiguration {

    @Autowired
    private ServerProperties serverProperties;

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory(serverProperties.getPort(), serverProperties.getContextPath());
    }

    @Bean
    public ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }

    @Bean
    public ErrorResponseCreator prepareErrorResponseCreator() {
        return new ErrorResponseCreator();
    }

    @Bean
    public JsonExceptionHandler jsonExceptionHandler() {
        return new JsonExceptionHandler(
                objectMapper(),
                prepareErrorResponseCreator(),
                serverProperties);
    }
}
