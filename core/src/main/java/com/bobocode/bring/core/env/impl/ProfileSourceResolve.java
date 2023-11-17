package com.bobocode.bring.core.env.impl;

import com.bobocode.bring.core.env.BringSourceLoader;
import com.bobocode.bring.core.env.BringSourceScanner;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class ProfileSourceResolve {

    private final BringSourceLoader bringSourceLoader;
    private final BringSourceScanner bringSourceScanner;

    public ProfileSourceResolve() {
        this.bringSourceLoader = new BringPropertySourceLoader();
        this.bringSourceScanner = new BringPropertiesSourceScanner();
    }

    public Map<String, String> resolve(String profileName, String type) {
        Map<String, String> properties = new HashMap<>();
        bringSourceScanner.scan(type).forEach(file -> {
            String name = file.getName();
            if (name.equals(BringPropertySourceLoader.APPLICATION_PROPERTIES) || (Objects.nonNull(profileName) && name.contains(profileName))) {
                properties.putAll(bringSourceLoader.load(file.getName()));
            }
        });

        return properties;
    }
}
