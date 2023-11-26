package com.bobocode.bring.web.utils;

import com.bobocode.bring.web.servlet.exception.MethodArgumentTypeMismatchException;
import com.bobocode.bring.web.servlet.exception.TypeArgumentUnsupportedException;
import lombok.experimental.UtilityClass;

/**
 * The {@code ParameterTypeUtils} class is a utility class that provides methods for parsing
 * path variables to their corresponding parameter types.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@UtilityClass
public class ParameterTypeUtils {

    /**
     * A constant string representing the boolean value "true".
     */
    public static final String TRUE = "true";

    /**
     * A constant string representing the boolean value "false".
     */
    public static final String FALSE = "false";

    /**
     * Parses the given path variable to the specified parameter type.
     *
     * @param pathVariable The string representation of the path variable.
     * @param type         The target parameter type to parse the path variable into.
     * @return An object of the specified parameter type representing the parsed path variable.
     * @throws MethodArgumentTypeMismatchException if the path variable cannot be converted to the required type.
     * @throws TypeArgumentUnsupportedException    if the specified parameter type is not supported.
     */
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
