package io.github.blyznytsiaorg.bring.core.injection.setter;

import io.github.blyznytsiaorg.bring.core.BringApplication;
import io.github.blyznytsiaorg.bring.core.exception.NoSuchBeanException;
import org.assertj.core.api.Assertions;
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
        Assertions.assertThat(aBean).isNotNull();
        Assertions.assertThat(bBean).isNotNull();
        Assertions.assertThat(bBean.getA()).isNotNull();
        Assertions.assertThat(bBean.getA()).isEqualTo(aBean);
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

    @DisplayName("Should throw exception when dependency implementation is missing via setter")
    @Test
    void shouldThrowExceptionWhenDependencyImplementationIsMissingViaSetter() {
        // given
        var expectedMessage = "No such bean that implements this interface testdata.di.negative.noimplementationviasetter.A ";


        Executable executable = () -> {
            //when
            BringApplication.run(TEST_DATA_PACKAGE + ".negative.noimplementationviasetter");
        };

        // then
        var noSuchBeanException = assertThrows(NoSuchBeanException.class, executable);
        assertThat(noSuchBeanException.getMessage()).isEqualTo(expectedMessage);
    }

    @DisplayName("Should inject implementations of Interface to Set Setter in appropriate Order")
    @Test
    void shouldInjectListOfInterfaceImplementationToSetSetterInOrder() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".positive.setsetterinjector");
        var children = Set.of(
                bringApplicationContext.getBean(testdata.di.positive.setsetterinjector.B.class),
                bringApplicationContext.getBean(testdata.di.positive.setsetterinjector.C.class)
        );

        // when
        var aBean = bringApplicationContext.getBean(ASetter.class);

        // then
        Assertions.assertThat(aBean).isNotNull();
        Assertions.assertThat(aBean.getSet()).isNotNull();
        Assertions.assertThat(aBean.getSet()).hasSize(2)
                .containsAll(children);
    }


    @DisplayName("Should inject implementations of Interface to Setter")
    @Test
    void shouldInjectListOfInterfaceImplementationToSetter() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".positive.listsetterinjector");

        // when
        var aBean = bringApplicationContext.getBean(testdata.di.positive.listsetterinjector.ASetter.class);

        // then
        Assertions.assertThat(aBean).isNotNull();
        Assertions.assertThat(aBean.getList()).isNotNull();
        Assertions.assertThat(aBean.getList()).hasSize(2);
    }
}
