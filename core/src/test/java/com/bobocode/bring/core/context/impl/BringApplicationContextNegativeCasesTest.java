package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.core.exception.CyclicBeanException;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import com.bobocode.bring.core.exception.NoUniqueBeanException;
import testdata.di.negative.configuration.A;
import testdata.di.negative.oneinterfacetwodependency.Barista;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BringApplicationContextNegativeCasesTest {

    private static final String DEMO_PACKAGE = "testdata.di.negative";

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
            BringApplication.run(DEMO_PACKAGE + ".configuration.nosuchbean");
        };

        // then
        assertThrows(NoSuchBeanException.class, executable);
    }

    @DisplayName("Should throw exception when trying to register two beans with same name")
    @Test
    void shouldThrowExceptionWhenNoUniqueBean() {
        // when
        Executable executable = () -> {
            //given
            BringApplication.run(DEMO_PACKAGE + ".configuration.nouniquebean");
        };

        // then
        assertThrows(NoUniqueBeanException.class, executable);
    }

    @DisplayName("Should throw exception when bean not from configuration class")
    @Test
    void shouldThrowExceptionWhenNotFromConfigClass() {
        // when
        BringApplicationContext context = BringApplication.run(DEMO_PACKAGE + ".configuration.noconfigbean");

        // then
        assertThrows(NoSuchBeanException.class,  () -> context.getBean(A.class));
    }

    @DisplayName("Should throw exception when bean not annotated with @Bean in configuration class")
    @Test
    void shouldThrowExceptionWhenMethodNotAnnotated() {
        // when
        BringApplicationContext context = BringApplication.run(DEMO_PACKAGE + ".configuration.notabean");

        // then
        assertThrows(NoSuchBeanException.class,  () -> context.getBean(A.class));
    }

    @DisplayName("Should throw exception when we have two dependencies for one interface")
    @Test
    void shouldThrowExceptionWhenWeHaveTwoDependenciesForOneInterface() {
        //given
        var expectedMessage = "No qualifying bean of type 'interface testdata.di.negative.oneinterfacetwodependency.Drink'" +
                " available: expected single matching bean but found 2: [Espresso,Latte]";
        //when
        Executable executable = () -> {
            var bringApplicationContext = BringApplication.run(DEMO_PACKAGE + ".oneinterfacetwodependency");

            bringApplicationContext.getBean(Barista.class);
        };

        // then
        NoUniqueBeanException noUniqueBeanException = assertThrows(NoUniqueBeanException.class, executable);
        assertThat(noUniqueBeanException.getMessage()).isEqualTo(expectedMessage);
    }
    
}