package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.anotation.Value;
import com.bobocode.bring.core.exception.PropertyValueNotFoundException;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * An abstract class that serves as a foundation for injecting property values based on annotations.
 * Implementations of this class are responsible for retrieving property values from a provided Map.
 */
public abstract class AbstractPropertyValueTypeInjector {

    private static final String VALUE_NOT_FOUND_MESSAGE = "Property value not found for the specified key: %s, field: %s.";
    private static final String DELIMITER = ":";
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
    public AbstractPropertyValueTypeInjector(Map<String, String> properties) {
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
        String value = properties.get(key);
        if (Objects.isNull(value) && COUNT_OF_PARAMETERS > keyAndDefaultValue.length) {
            throw new PropertyValueNotFoundException(String.format(VALUE_NOT_FOUND_MESSAGE, key, fieldName));
        }

        return Optional.ofNullable(value).orElseGet(() -> keyAndDefaultValue[VALUE_INDEX]);
    }

}
