package io.github.blyznytsiaorg.bring.core.injection.cyclic;

import io.github.blyznytsiaorg.bring.core.BringApplication;
import io.github.blyznytsiaorg.bring.core.exception.CyclicBeanException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import testdata.di.negative.fieldcyclic.A;
import testdata.di.negative.fieldcyclic.B;
import testdata.di.negative.fieldcyclic.C;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BringApplicationContextCyclicDependencyTest {

    private static final String DEMO_PACKAGE = "testdata.di.negative";

    @DisplayName("Should throw exception when we have Cyclic dependency via constructor")
    @Test
    void shouldThrowExceptionWhenWeHaveCyclicDependencyViaConstructor() {
        //given
        var expectedMessage = "Looks like you have cyclic dependency between those beans [a -> b -> c -> a]";


        // when
        Executable executable = () -> {
            //given
            BringApplication.run(DEMO_PACKAGE + ".cyclic");
        };

        // then
        CyclicBeanException cyclicBeanException = assertThrows(CyclicBeanException.class, executable);
        assertThat(cyclicBeanException.getMessage()).isEqualTo(expectedMessage);
    }

    @DisplayName("Should not throw exception when we have Cyclic dependency via field")
    @Test
    void shouldNotThrowExceptionWhenWeHaveCyclicDependencyViaField() {
        // when
        var bringApplicationContext = BringApplication.run(DEMO_PACKAGE + ".fieldcyclic");

        //then

        A aBean = bringApplicationContext.getBean(A.class);
        B bBean = bringApplicationContext.getBean(B.class);
        C cBean = bringApplicationContext.getBean(C.class);

        assertThat(bBean).isNotNull();
        assertThat(aBean).isNotNull();
        assertThat(cBean).isNotNull();

        assertThat(bBean.getA()).isNotNull();
        assertThat(bBean.getA()).isEqualTo(aBean);
    }

    @DisplayName("Should not throw exception when we have Cyclic dependency via setter")
    @Test
    void shouldNotThrowExceptionWhenWeHaveCyclicDependencyViaSetter() {
        // when
        var bringApplicationContext = BringApplication.run(DEMO_PACKAGE + ".settercyclic");

        //then

        var aBean = bringApplicationContext.getBean(testdata.di.negative.settercyclic.A.class);
        var bBean = bringApplicationContext.getBean(testdata.di.negative.settercyclic.B.class);
        var cBean = bringApplicationContext.getBean(testdata.di.negative.settercyclic.C.class);

        Assertions.assertThat(bBean).isNotNull();
        Assertions.assertThat(aBean).isNotNull();
        Assertions.assertThat(cBean).isNotNull();

        Assertions.assertThat(bBean.getA()).isNotNull();
        Assertions.assertThat(bBean.getA()).isEqualTo(aBean);
    }
}
