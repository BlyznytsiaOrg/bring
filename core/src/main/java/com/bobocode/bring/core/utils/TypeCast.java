package com.bobocode.bring.core.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TypeCast {

    private static final char NULL_VALUE = '\u0000';

    public static Object cast(Object value, Class<?> type) {
        if (value == null || type == null) {
            return null;
        }

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

        return value;
    }
}
