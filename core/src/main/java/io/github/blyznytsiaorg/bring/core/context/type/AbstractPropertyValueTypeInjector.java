package io.github.blyznytsiaorg.bring.core.context.type;

import io.github.blyznytsiaorg.bring.core.annotation.Value;
import io.github.blyznytsiaorg.bring.core.exception.PropertyValueNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * An abstract class that serves as a foundation for injecting property values based on annotations.
 * Implementations of this class are responsible for retrieving property values from a provided Map.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Slf4j
public abstract class AbstractPropertyValueTypeInjector {

    private static final String VALUE_NOT_FOUND_MESSAGE = "Property value not found for the specified key: %s, field: %s.";
    private static final String DELIMITER = ":";
    private static final String EMPTY_STRING = "";
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;
    private static final int COUNT_OF_PARAMETERS = 2;

    // Map containing property values
    private final Map<String, String> properties;

    /**
     * Constructs an AbstractPropertyValueTypeInjector with a given set of properties.
     *
     * @param properties a Map containing property key-value pairs
     */
    protected AbstractPropertyValueTypeInjector(Map<String, String> properties) {
        this.properties = properties;
    }

    /**
     * Retrieves the property value for a given field based on the Value annotation.
     * If the value is not found, it falls back to the default value specified in the annotation.
     *
     * @param valueAnnotation the Value annotation providing the property key and default value
     * @param fieldName       the name of the field for which the property value is retrieved
     * @return the property value for the specified field, or the default value if not found
     * @throws PropertyValueNotFoundException if the property value is not found and no default value is provided
     */
    public String getValue(Value valueAnnotation, String fieldName) {
        String[] keyAndDefaultValue = valueAnnotation.value().split(DELIMITER);
        String key = keyAndDefaultValue[KEY_INDEX];
        String value = Optional.ofNullable(properties.get(key))
                .orElseGet(() -> getDefaultValue(valueAnnotation, keyAndDefaultValue));
        if (Objects.isNull(value)) {
            log.error(String.format(VALUE_NOT_FOUND_MESSAGE, key, fieldName));
            throw new PropertyValueNotFoundException(String.format(VALUE_NOT_FOUND_MESSAGE, key, fieldName));
        }

        return value;
    }

    private String getDefaultValue(Value valueAnnotation, String[] keyAndDefaultValue) {
        if (valueAnnotation.value().contains(DELIMITER)) {
            if (COUNT_OF_PARAMETERS > keyAndDefaultValue.length) {
                return EMPTY_STRING;
            }
            return keyAndDefaultValue[VALUE_INDEX];
        }

        return null;
    }

}
