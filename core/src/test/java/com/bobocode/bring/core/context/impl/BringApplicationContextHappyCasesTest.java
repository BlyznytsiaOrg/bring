package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.testdata.di.positive.contract.Barista;
import com.bobocode.bring.testdata.di.positive.setter.A;
import com.bobocode.bring.testdata.di.positive.setter.B;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BringApplicationContextHappyCasesTest {

    private static final String TEST_DATA_PACKAGE = "com.bobocode.bring.testdata.di.positive";

    @DisplayName("Should found one interface and inject it")
    @Test
    void shouldFoundOneInterfaceAndInjectIt() {
        //given
        var bringApplicationContext = new BringApplicationContext(TEST_DATA_PACKAGE + ".contract");
        String expectedMessage = "Barista is preparing a drink: Making a delicious latte!";

        //when
        var barista = bringApplicationContext.getBean(Barista.class);

        //then
        assertThat(barista.prepareDrink()).isEqualTo(expectedMessage);
    }

    @DisplayName("Should inject dependency when you forget to add autowired and you have one constructor")
    @Test
    void shouldInjectConstructorDependencyIfYouHaveOneAndForgetToAddAutowired() {
        // given
        var bringApplicationContext = new BringApplicationContext(TEST_DATA_PACKAGE +".constructor");

        // when
        var aBean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.constructor.A.class);
        var bBean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.constructor.B.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(bBean).isNotNull();
        assertThat(bBean.getA()).isNotNull();
        assertThat(bBean.getA()).isEqualTo(aBean);
    }

    @DisplayName("Should inject dependency when you have multiple constructors but one of it was market as autowired")
    @Test
    void shouldInjectDependencyWhenYouHaveMultipleConstructorButOneOfItWasMarketAsAutowired() {
        // given
        var bringApplicationContext = new BringApplicationContext(TEST_DATA_PACKAGE +".secondconstructor");

        // when
        var aBean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.secondconstructor.A.class);
        var bBean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.secondconstructor.B.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(bBean).isNotNull();
        assertThat(bBean.getA()).isNotNull();
        assertThat(bBean.getA()).isEqualTo(aBean);
    }

    @DisplayName("Should inject dependency via Field")
    @Test
    void shouldInjectDependencyViaField() {
        // given
        var bringApplicationContext = new BringApplicationContext(TEST_DATA_PACKAGE +".field");

        // when
        var aBean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.field.A.class);
        var bBean = bringApplicationContext.getBean(com.bobocode.bring.testdata.di.positive.field.B.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(bBean).isNotNull();
        assertThat(bBean.getA()).isNotNull();
        assertThat(bBean.getA()).isEqualTo(aBean);
    }

    @DisplayName("Should inject dependency via setter")
    @Test
    void shouldInjectDependencyViaSetter() {
        // given
        var bringApplicationContext = new BringApplicationContext(TEST_DATA_PACKAGE + ".setter");

        // when
        var aBean = bringApplicationContext.getBean(A.class);
        var bBean = bringApplicationContext.getBean(B.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(bBean).isNotNull();
        assertThat(bBean.getA()).isNotNull();
        assertThat(bBean.getA()).isEqualTo(aBean);
    }

}