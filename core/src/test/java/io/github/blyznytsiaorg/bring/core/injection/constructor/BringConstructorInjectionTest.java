package io.github.blyznytsiaorg.bring.core.injection.constructor;

import io.github.blyznytsiaorg.bring.core.BringApplication;
import io.github.blyznytsiaorg.bring.core.exception.NoConstructorWithAutowiredAnnotationBeanException;
import io.github.blyznytsiaorg.bring.core.exception.NoSuchBeanException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import testdata.di.positive.contract.Barista;
import testdata.di.positive.setconstructorinjector.AConstructor;

import java.util.Set;

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
        Assertions.assertThat(barista.prepareDrink()).isEqualTo(expectedMessage);
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
        Assertions.assertThat(aBean).isNotNull();
        Assertions.assertThat(bBean).isNotNull();
        Assertions.assertThat(bBean.getA()).isNotNull();
        Assertions.assertThat(bBean.getA()).isEqualTo(aBean);
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
        Assertions.assertThat(aBean).isNotNull();
        Assertions.assertThat(bBean).isNotNull();
        Assertions.assertThat(bBean.getA()).isNotNull();
        Assertions.assertThat(bBean.getA()).isEqualTo(aBean);
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

    @DisplayName("Should inject implementations of Interface to Set Constructor in appropriate Order")
    @Test
    void shouldInjectListOfInterfaceImplementationToSetConstructorInOrder() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".positive.setconstructorinjector");

        var children = Set.of(
                bringApplicationContext.getBean(testdata.di.positive.setconstructorinjector.B.class),
                bringApplicationContext.getBean(testdata.di.positive.setconstructorinjector.D.class),
                bringApplicationContext.getBean(testdata.di.positive.setconstructorinjector.C.class)
        );

        // when
        var aBean = bringApplicationContext.getBean(AConstructor.class);

        // then
        Assertions.assertThat(aBean).isNotNull();
        Assertions.assertThat(aBean.getSet()).isNotNull();
        Assertions.assertThat(aBean.getSet()).hasSize(3)
                .containsAll(children);
    }

    @DisplayName("Should inject implementations of Interface to Constructor")
    @Test
    void shouldInjectListOfInterfaceImplementationToConstructor() {
        // given
        var bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".positive.listconstructorinjector");

        // when
        var aBean = bringApplicationContext.getBean(testdata.di.positive.listconstructorinjector.AConstructor.class);

        // then
        Assertions.assertThat(aBean).isNotNull();
        Assertions.assertThat(aBean.getList()).isNotNull();
        Assertions.assertThat(aBean.getList()).hasSize(2);
    }

}
