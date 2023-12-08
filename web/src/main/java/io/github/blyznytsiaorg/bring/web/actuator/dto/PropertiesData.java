package io.github.blyznytsiaorg.bring.web.actuator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * DTO class representing system properties data.
 * Contains a map of property names and values.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Setter
@Getter
@ToString
@Builder
public class PropertiesData {
    /**
     * A map representing system properties, where keys are property names and values are property values.
     */
    private Map<String, String> properties;
}
