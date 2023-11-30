package com.bobocode.bring.core.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;


/**
 * The TypeCast class provides methods to perform type casting operations
 * between different object types in Java.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Slf4j
@UtilityClass
public class TypeCast {

    /**
     * Represents the null character value.
     */
    private final char NULL_VALUE = '\u0000';

    /**
     * Casts the provided value to the specified type.
     *
     * @param value the object value to be casted
     * @param type the target class type to which the value should be casted
     * @return the value casted to the specified type, or null if the value or type is null,
     * or if no suitable casting is available
     */
    public Object cast(Object value, Class<?> type) {
        if (value == null || type == null) {
            return null;
        }

        try {
            if (type.equals(Integer.class) || type.equals(int.class)) {
                return Integer.parseInt(value.toString());
            } else if (type.equals(Double.class) || type.equals(double.class)) {
                return Double.parseDouble(value.toString());
            } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
                return Boolean.parseBoolean(value.toString());
            } else if (type.equals(Byte.class) || type.equals(byte.class)) {
                return Byte.parseByte(value.toString());
            } else if (type.equals(Short.class) || type.equals(short.class)) {
                return Short.parseShort(value.toString());
            } else if (type.equals(Long.class) || type.equals(long.class)) {
                return Long.parseLong(value.toString());
            } else if (type.equals(Float.class) || type.equals(float.class)) {
                return Float.parseFloat(value.toString());
            } else if (type.equals(Character.class) || type.equals(char.class)) {
                String stringValue = value.toString();
                if (!stringValue.isEmpty()) {
                    return stringValue.charAt(0);
                }
                return NULL_VALUE;
            }
        } catch (Exception exe) {
            log.warn("Cannot cast value {} to type {} message {}", value, type, exe.getMessage());
            return null;
        }

        return value;
    }
}
