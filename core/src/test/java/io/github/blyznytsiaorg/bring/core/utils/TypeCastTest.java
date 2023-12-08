package io.github.blyznytsiaorg.bring.core.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TypeCastTest {

    static Stream<Object[]> provideTestCases() {
        return Stream.of(
                new Object[]{"42", Integer.class, 42},
                new Object[]{"3.14", Double.class, 3.14},
                new Object[]{"true", Boolean.class, true},
                new Object[]{"X", Character.class, 'X'},
                new Object[]{"127", Byte.class, (byte) 127},
                new Object[]{"1000", Short.class, (short) 1000},
                new Object[]{"123456789012", Long.class, 123456789012L},
                new Object[]{"3.14", Float.class, 3.14f},
                new Object[]{null, Integer.class, null},
                new Object[]{"InvalidValue", Integer.class, null}
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void shouldTestCast(Object value, Class<?> type, Object expectedResult) {
        // When
        Object result = TypeCast.cast(value, type);

        // Then
        assertEquals(expectedResult, result);
    }

}