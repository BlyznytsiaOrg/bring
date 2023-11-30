package com.bobocode.bring.core.utils;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class PairTest {

    @Test
    void shouldPairOfMethodWithNullValues() {
        //given
        String leftValue = null;
        Integer rightValue = null;

        //when
        Pair<String, Integer> pair = Pair.of(leftValue, rightValue);

        //then
        assertThat(pair.left).isNull();
        assertThat(pair.right).isNull();
    }

    @Test
    void shouldPairOfMethodWithDifferentTypes() {
        //given
        Double leftValue = 3.14;
        Long rightValue = 100L;

        //when
        Pair<Double, Long> pair = Pair.of(leftValue, rightValue);

        //then
        assertThat(pair.left).isEqualTo(leftValue);
        assertThat(pair.right).isEqualTo(rightValue);
    }

    @Test
    void shouldPairOfMethodWithEqualValues() {
        //given
        String value = "Hello";

        //when
        Pair<String, String> pair = Pair.of(value, value);

        //then
        assertThat(pair.left).isEqualTo(value);
        assertThat(pair.right).isEqualTo(value);
    }
}
