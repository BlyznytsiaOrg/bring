package com.bobocode.bring.core.injection.field;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BringFieldInjectionTest {

    private static final String TEST_DATA_PACKAGE = "testdata.di";

    @DisplayName("Should inject dependency via Field")
    @Test
    void shouldInjectDependencyViaField() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE +".positive.field");

        // when
        var aBean = bringApplicationContext.getBean(testdata.di.positive.field.A.class);
        var bBean = bringApplicationContext.getBean(testdata.di.positive.field.B.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(bBean).isNotNull();
        assertThat(bBean.getA()).isNotNull();
        assertThat(bBean.getA()).isEqualTo(aBean);
    }


    @DisplayName("Should throw exception when field injection is not possible")
    @Test
    void shouldThrowExceptionWhenFieldInjectIsNotPossible() {
        //given
        var expectedMessage = "Not such bean found exception class testdata.di.negative.field.A";

        Executable executable = () -> {
            //when
            BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".negative.field");
            var bBean = bringApplicationContext.getBean(testdata.di.negative.field.B.class);
        };

        // then
        var noSuchBeanException = assertThrows(NoSuchBeanException.class, executable);
        assertThat(noSuchBeanException.getMessage()).isEqualTo(expectedMessage);
    }

    @DisplayName("Should throw exception when field injection is not possible via interface no implementation found")
    @Test
    void shouldThrowExceptionWhenFieldInjectIsNotPossibleViaInterfaceNoImplementationFound() {
        //given
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".negative.nofieldinterfaceimplementation");
        var expectedMessage = "No such bean that implements this interface testdata.di.negative.nofieldinterfaceimplementation.A ";
        var bBean = bringApplicationContext.getBean(testdata.di.negative.nofieldinterfaceimplementation.B.class);

        Executable executable = () -> {
            //when
            bringApplicationContext.getBean(testdata.di.negative.nofieldinterfaceimplementation.A.class);
        };

        // then
        assertThat(bBean).isNotNull();
        assertThat(bBean.getA()).isNull();
        var noSuchBeanException = assertThrows(NoSuchBeanException.class, executable);
        assertThat(noSuchBeanException.getMessage()).isEqualTo(expectedMessage);
    }
}
