package io.github.blyznytsiaorg.bring.core.env.impl;

import io.github.blyznytsiaorg.bring.core.env.BringSourceLoader;
import io.github.blyznytsiaorg.bring.core.env.BringSourceScanner;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class responsible for resolving profile sources using BringSourceLoader and BringSourceScanner.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Slf4j
public class ProfileSourceResolve {

    private final BringSourceLoader bringSourceLoader;
    private final BringSourceScanner bringSourceScanner;

    /**
     * Initializes ProfileSourceResolve with default BringSourceLoader and BringSourceScanner implementations.
     */
    public ProfileSourceResolve() {
        this.bringSourceLoader = new BringPropertySourceLoader();
        this.bringSourceScanner = new BringPropertiesSourceScanner();
    }

    /**
     * Resolves properties for a specific profile and type.
     *
     * @param profileName The name of the profile to resolve.
     * @param type        The type of properties to resolve.
     * @return A map containing properties resolved for the specified profile and type.
     */
    public Map<String, String> resolve(String profileName, String type) {
        log.trace("Resolve profileName {} type {}", profileName, type);
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
