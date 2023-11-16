package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.core.exception.CyclicBeanException;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BringApplicationContextNegativeCasesTest {

    private static final String DEMO_PACKAGE = "com.bobocode.bring.testdata.di.negative";

    @DisplayName("Should throw exception when we have Cyclic dependency")
    @Test
    void shouldThrowExceptionWhenWeHaveCyclicDependency() {

        // when
        Executable executable = () -> {
            //given
            BringApplication.run(DEMO_PACKAGE + ".cyclic");
        };

        // then
        assertThrows(CyclicBeanException.class, executable);
    }

    @DisplayName("Should throw exception when injecting into configuration method when no bean with param name")
    @Test
    void shouldThrowExceptionWhenNoBeanForParameterName() {

        // when
        Executable executable = () -> {
            //given
            BringApplication.run(DEMO_PACKAGE + ".configuration");
        };

        // then
        assertThrows(NoSuchBeanException.class, executable);
    }
    
}