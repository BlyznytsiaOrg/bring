package com.bobocode.bring.core.env.impl;

import com.bobocode.bring.core.env.BringSourceLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;


/**
 * Implementation of BringSourceLoader for loading properties from files.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Slf4j
public class BringPropertySourceLoader implements BringSourceLoader {

    public static final String APPLICATION_PROPERTIES = "application.properties";
    public static final String RANDOM_PORT_KEY = "${random.port}";
    public static final int START_PORT_RANGE = 8000;
    public static final int END_PORT_RANGE = 9000;

    /**
     * Returns the file extension handled by this loader.
     *
     * @return The file extension, which is "properties".
     */
    @Override
    public String getFileExtensions() {
        return "properties";
    }

    /**
     * Loads properties from the default file ("application.properties") and optionally from another specified file,
     * merging them where the latter overrides the former.
     *
     * @param name The name of the file containing additional properties. If null, defaults to "application.properties".
     * @return A map containing the merged properties from both sources.
     */
    @Override
    public Map<String, String> load(String name) {
        Map<String, String> defaultProperties = loadProperties(APPLICATION_PROPERTIES);
        Map<String, String> overrideProperties = loadProperties(name);
        defaultProperties.putAll(overrideProperties);
        return defaultProperties;
    }


    /**
     * Loads properties from a specified file.
     *
     * @param fileName The name of the file to load properties from. If null, defaults to "application.properties".
     * @return A map containing the properties loaded from the specified file.
     */
    private Map<String, String> loadProperties(String fileName) {
        String defaultFileName  = (fileName == null) ? APPLICATION_PROPERTIES : fileName;
        log.debug("Load Property file {}", defaultFileName);
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
                    .collect(toMap(key -> key, getValue(properties), (a, b) -> b));

        } catch (IOException exe) {
            log.error("Error loading properties from file {}: {}. Will use default one {}", defaultFileName,
                    exe.getMessage(), APPLICATION_PROPERTIES);
        }

        return propertiesMap;
    }

    private Function<String, String> getValue(Properties properties) {
        return key -> Optional.ofNullable(properties.getProperty(key))
                .filter(RANDOM_PORT_KEY::equals)
                .map(value -> String.valueOf(ThreadLocalRandom.current().nextInt(START_PORT_RANGE, END_PORT_RANGE)))
                .orElse(properties.getProperty(key));
    }
}
