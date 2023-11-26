package com.bobocode.bring.core.injection.constructor;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.core.exception.NoConstructorWithAutowiredAnnotationBeanException;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import testdata.di.positive.contract.Barista;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BringConstructorInjectionTest {

    private static final String TEST_DATA_PACKAGE = "testdata.di";

    @DisplayName("Should found one interface and inject it via constructor")
    @Test
    void shouldFoundOneInterfaceAndInjectItViaConstructor() {
        //given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".positive.contract");
        String expectedMessage = "Barista is preparing a drink: Making a delicious latte!";

        //when
        var barista = bringApplicationContext.getBean(Barista.class);

        //then
        assertThat(barista.prepareDrink()).isEqualTo(expectedMessage);
    }

    @DisplayName("Should throw exception when we have interface and don't have implementation")
    @Test
    void shouldThrowExceptionWhenWeHaveInterfaceAndDontHaveImplementation() {
        //given
        var expectedMessage = "No such bean that implements this interface testdata.di.negative.noimplementation.Drink ";

        Executable executable = () -> {
            //when
            BringApplication.run(TEST_DATA_PACKAGE + ".negative.noimplementation");
        };

        // then
        var noSuchBeanException = assertThrows(NoSuchBeanException.class, executable);
        assertThat(noSuchBeanException.getMessage()).isEqualTo(expectedMessage);
    }

    @DisplayName("Should throw exception when we have class but not add annotation to it like component")
    @Test
    void shouldThrowExceptionWhenWeHaveClassButNotAddAnyAnnotationToItLikeComponent() {
        //given
        var expectedMessage = "Not such bean found exception class testdata.di.negative.noclassimplementation.Latte";

        Executable executable = () -> {
            //when
            BringApplication.run(TEST_DATA_PACKAGE + ".negative.noclassimplementation");
        };

        // then
        var noSuchBeanException = assertThrows(NoSuchBeanException.class, executable);
        assertThat(noSuchBeanException.getMessage()).isEqualTo(expectedMessage);
    }

    @DisplayName("Should inject dependency when you forget to add autowired and you have one constructor")
    @Test
    void shouldInjectConstructorDependencyIfYouHaveOneAndForgetToAddAutowired() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE +".positive.constructor");

        // when
        var aBean = bringApplicationContext.getBean(testdata.di.positive.constructor.A.class);
        var bBean = bringApplicationContext.getBean(testdata.di.positive.constructor.B.class);

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
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE +".positive.secondconstructor");

        // when
        var aBean = bringApplicationContext.getBean(testdata.di.positive.secondconstructor.A.class);
        var bBean = bringApplicationContext.getBean(testdata.di.positive.secondconstructor.B.class);

        // then
        assertThat(aBean).isNotNull();
        assertThat(bBean).isNotNull();
        assertThat(bBean.getA()).isNotNull();
        assertThat(bBean.getA()).isEqualTo(aBean);
    }

    @DisplayName("Should throw exception when you have multiple constructors but no one of it was market as autowired")
    @Test
    void shouldThrowExceptionWhenYouYouHaveMultipleConstructorButNoOneOfItWasMarketAsAutowired() {
        //given
        var expectedMessage = "No constructor with Autowired annotation for class testdata.di.negative.secondconstructor.B. \n" +
                " Add Autowired to one of them [public testdata.di.negative.secondconstructor.B(), public testdata.di.negative.secondconstructor.B(testdata.di.negative.secondconstructor.A)]";

        Executable executable = () -> {
            //when
            BringApplication.run(TEST_DATA_PACKAGE +".negative.secondconstructor");
        };

        // then
        var noSuchBeanException = assertThrows(NoConstructorWithAutowiredAnnotationBeanException.class, executable);
        assertThat(noSuchBeanException.getMessage()).isEqualTo(expectedMessage);
    }


}
