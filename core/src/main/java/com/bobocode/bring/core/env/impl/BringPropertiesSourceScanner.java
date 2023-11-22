package com.bobocode.bring.core.env.impl;

import com.bobocode.bring.core.env.BringSourceScanner;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Slf4j
public class BringPropertiesSourceScanner implements BringSourceScanner {
    private static final String APPLICATION = "application";

    @Override
    public List<File> scan(String type) {
        return loadAllProperties(type);
    }

    @SneakyThrows
    private List<File> loadAllProperties(String type) {
        List<File> refFiles = new ArrayList<>();
        String applicationProperties = APPLICATION + type;

        URL resource = getClass().getClassLoader().getResource(applicationProperties);
        if (Objects.isNull(resource)) {
            log.warn("No resources directory found in classpath");
            return refFiles;
        }

        Path folderPath = Paths.get(resource.toURI()).resolve("..").normalize();

        File[] files = folderPath.toFile().listFiles();

        if (Objects.nonNull(files)) {
            refFiles = Arrays.stream(files)
                    .filter(file -> file.isFile() && file.getName().endsWith(type))
                    .sorted(Comparator.comparing(File::getName).reversed())
                    .toList();
        }

        return refFiles;
    }
}
