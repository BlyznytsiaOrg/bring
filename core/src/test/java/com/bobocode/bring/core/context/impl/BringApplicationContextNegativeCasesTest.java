package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.core.exception.CyclicBeanException;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import com.bobocode.bring.core.exception.NoUniqueBeanException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BringApplicationContextNegativeCasesTest {

    private static final String DEMO_PACKAGE = "com.bobocode.bring.testdata.di.negative";

    @DisplayName("Should throw exception when we have Cyclic dependency")
    @Test
    void shouldThrowExceptionWhenWeHaveCyclicDependency() {
        //given
        var expectedMessage = "Looks like you have cyclic dependency between those beans [A, B]";

        // when
        Executable executable = () -> {
            //given
            BringApplication.run(DEMO_PACKAGE + ".cyclic");
        };

        // then
        CyclicBeanException cyclicBeanException = assertThrows(CyclicBeanException.class, executable);
        assertThat(cyclicBeanException.getMessage()).isEqualTo(expectedMessage);

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

    @DisplayName("Should throw exception when we have two dependencies for one interface")
    @Test
    void shouldThrowExceptionWhenWeHaveTwoDependenciesForOneInterface() {
        //given
        var expectedMessage = "No qualifying bean of type 'interface com.bobocode.bring.testdata.di.negative.oneinterfacetwodependency.Drink'" +
                " available: expected single matching bean but found 2: [Latte, Espresso]";
        //when
        Executable executable = () -> {
            var bringApplicationContext = BringApplication.run(DEMO_PACKAGE + ".oneinterfacetwodependency");

            bringApplicationContext.getBean(com.bobocode.bring.testdata.di.negative.oneinterfacetwodependency.Barista.class);
        };

        // then
        NoUniqueBeanException noUniqueBeanException = assertThrows(NoUniqueBeanException.class, executable);
        assertThat(noUniqueBeanException.getMessage()).isEqualTo(expectedMessage);
    }
    
}