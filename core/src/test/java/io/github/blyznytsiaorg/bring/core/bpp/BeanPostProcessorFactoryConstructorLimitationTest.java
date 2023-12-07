package io.github.blyznytsiaorg.bring.core.bpp;

import io.github.blyznytsiaorg.bring.core.BringApplication;
import io.github.blyznytsiaorg.bring.core.exception.BeanPostProcessorConstructionLimitationException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanPostProcessorFactoryConstructorLimitationTest {

    @SneakyThrows
    @DisplayName("Should throw exception when bpp without default constructor")
    @Test
    void shouldThrowExceptionWhenBppWithoutDefaultConstructor() {
        //given
        var expectedMessage = "BeanProcessor 'NoDefaultConstructorBeanPostProcessor' should have only default constructor without params";

        Executable executable = () -> {
            //when
            BringApplication.run("testdata.bpp", "testdata.bpp");
        };

        // then
        var noSuchBeanException = assertThrows(BeanPostProcessorConstructionLimitationException.class, executable);
        assertThat(noSuchBeanException.getMessage()).isEqualTo(expectedMessage);

    }
}
