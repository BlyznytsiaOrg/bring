package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.core.exception.NoUniqueBeanException;
import com.bobocode.bring.testdata.di.negative.contract.Barista;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BringApplicationContextNegativeCasesTest {

    private static final String DEMO_PACKAGE = "com.bobocode.bring.testdata.di.negative";

    @Disabled
    @DisplayName("Should throw no unique bean exception when found two implementation of one interface")
    @Test
    void shouldThrowNoUniqueBeanException() {

        // when
        Executable executable = () -> {
            //given
            var bringApplicationContext = BringApplication.run(DEMO_PACKAGE + ".contract");
            bringApplicationContext.getBean(Barista.class);
        };

        // then
        assertThrows(NoUniqueBeanException.class, executable);
    }
}