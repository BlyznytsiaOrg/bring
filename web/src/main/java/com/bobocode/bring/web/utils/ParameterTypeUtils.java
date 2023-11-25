package com.bobocode.bring.web.utils;

import com.bobocode.bring.web.servlet.exception.MethodArgumentTypeMismatchException;
import com.bobocode.bring.web.servlet.exception.TypeArgumentUnsupportedException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ParameterTypeUtils {

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static Object parseToParameterType(String pathVariable, Class<?> type) {
        Object obj;
        try {
            if (type.equals(String.class)) {
                obj = pathVariable;
            } else if (type.equals(Long.class) || type.equals(long.class)) {
                obj = Long.parseLong(pathVariable);
            } else if (type.equals(Double.class) || type.equals(double.class)) {
                obj = Double.parseDouble(pathVariable);
            } else if (type.equals(Float.class) || type.equals(float.class)) {
                obj = Float.parseFloat(pathVariable);
            } else if (type.equals(Integer.class) || type.equals(int.class)) {
                obj = Integer.parseInt(pathVariable);
            } else if (type.equals(Byte.class) || type.equals(byte.class)) {
                obj = Byte.parseByte(pathVariable);
            } else if (type.equals(Short.class) || type.equals(short.class)) {
                obj = Short.parseShort(pathVariable);
            } else if (type.equals(Character.class) || type.equals(char.class)) {
               obj = pathVariable.charAt(0);
            } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
                if (pathVariable.equals(TRUE)) {
                    obj = Boolean.TRUE;
                } else if (pathVariable.equals(FALSE)) {
                    obj = Boolean.FALSE;
                } else {
                    throw new MethodArgumentTypeMismatchException(
                            String.format("Failed to convert value of type 'java.lang.String' "
                                    + "to required type '%s'; Invalid value [%s]", type.getName(), pathVariable));
                }
            } else {
                throw new TypeArgumentUnsupportedException(
                        String.format("The type parameter: '%s' is not supported", type.getName()));
            }
            return obj;
        } catch (NumberFormatException exception) {
            throw new MethodArgumentTypeMismatchException(
                    String.format("Failed to convert value of type 'java.lang.String' "
                            + "to required type '%s'; Invalid value [%s]", type.getName(), pathVariable));
        }
    }
}
