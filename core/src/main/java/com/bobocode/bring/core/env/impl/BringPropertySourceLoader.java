package com.bobocode.bring.core.env.impl;

import com.bobocode.bring.core.env.BringSourceLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static java.util.stream.Collectors.toMap;

@Slf4j
public class BringPropertySourceLoader implements BringSourceLoader {

    public static final String APPLICATION_PROPERTIES = "application.properties";

    @Override
    public String getFileExtensions() {
        return "properties";
    }

    @Override
    public Map<String, String> load(String name) {
        Map<String, String> defaultProperties = loadProperties(APPLICATION_PROPERTIES);
        Map<String, String> overrideProperties = loadProperties(name);
        defaultProperties.putAll(overrideProperties);
        return defaultProperties;
    }


    private static Map<String, String> loadProperties(String fileName) {
        String defaultFileName  = (fileName == null) ? APPLICATION_PROPERTIES : fileName;
        Map<String, String> propertiesMap = new HashMap<>();

        try (InputStream source = BringPropertySourceLoader.class.getClassLoader().getResourceAsStream(defaultFileName)) {
            if (Objects.isNull(source)) {
                log.warn("Cannot find file {}. Will use default one {}", fileName, APPLICATION_PROPERTIES);
                return propertiesMap;
            }

            Properties properties = new Properties();
            properties.load(source);

            propertiesMap = properties.stringPropertyNames()
                    .stream()
                    .collect(toMap(key -> key, properties::getProperty, (a, b) -> b));

        } catch (IOException exe) {
            log.error("Error loading properties from file {}: {}. Will use default one {}", defaultFileName,
                    exe.getMessage(), APPLICATION_PROPERTIES);
        }

        return propertiesMap;
    }
}
