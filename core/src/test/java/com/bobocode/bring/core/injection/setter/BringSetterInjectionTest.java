package com.bobocode.bring.core.injection.setter;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import testdata.di.positive.setsetterinjector.ASetter;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BringSetterInjectionTest {

    private static final String TEST_DATA_PACKAGE = "testdata.di";

    @DisplayName("Should inject dependency via setter")
    @Test
    void shouldInjectDependencyViaSetter() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".positive.setter");

        // when
        var aBean = bringApplicationContext.getBean(testdata.di.positive.setter.A.class);
        var bBean = bringApplicationContext.getBean(testdata.di.positive.setter.B.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(bBean).isNotNull();
        assertThat(bBean.getA()).isNotNull();
        assertThat(bBean.getA()).isEqualTo(aBean);
    }

    @DisplayName("Should throw exception when dependency class is missing via setter")
    @Test
    void shouldThrowExceptionWhenDependencyIsMissingViaSetter() {
        // given
        var expectedMessage = "Not such bean found exception class testdata.di.negative.setter.A";

        Executable executable = () -> {
            //when
            BringApplication.run(TEST_DATA_PACKAGE + ".negative.setter");
        };

        // then
        var noSuchBeanException = assertThrows(NoSuchBeanException.class, executable);
        assertThat(noSuchBeanException.getMessage()).isEqualTo(expectedMessage);
    }

    @DisplayName("Should not set dependency implementation is missing via setter")
    @Test
    void shouldThrowExceptionWhenDependencyImplementationIsMissingViaSetter() {
        // given
        var expectedMessage = "No such bean that implements this interface testdata.di.negative.noimplementationviasetter.A ";

        //when
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".negative.noimplementationviasetter");


        var bBean = bringApplicationContext.getBean(testdata.di.negative.noimplementationviasetter.B.class);


        Executable executable = () -> {
            //when
             bringApplicationContext.getBean(testdata.di.negative.noimplementationviasetter.A.class);
        };

        // then
        assertThat(bBean).isNotNull();
        assertThat(bBean.getA()).isNull();

        var noSuchBeanException = assertThrows(NoSuchBeanException.class, executable);
        assertThat(noSuchBeanException.getMessage()).isEqualTo(expectedMessage);
    }

    @DisplayName("Should inject implementations of Interface to Set Constructor in appropriate Order")
    @Test
    void shouldInjectListOfInterfaceImplementationToSetFieldInOrder() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".positive.setsetterinjector");
        var children = Set.of(
                testdata.di.positive.setsetterinjector.B.class,
                testdata.di.positive.setsetterinjector.C.class);

        // when
        var aBean = bringApplicationContext.getBean(ASetter.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(aBean.getSet()).isNotNull();
        assertThat(aBean.getSet()).allSatisfy(child -> assertThat(children).contains(child.getClass()));
    }


    @DisplayName("Should inject implementations of Interface to Setter")
    @Test
    void shouldInjectListOfInterfaceImplementationToSetter() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".positive.listsetterinjector");

        // when
        var aBean = bringApplicationContext.getBean(testdata.di.positive.listsetterinjector.ASetter.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(aBean.getList()).isNotNull();
        assertThat(aBean.getList()).hasSize(2);
    }
}
