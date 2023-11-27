package com.bobocode.bring.core.bfpp;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.core.exception.BeanPostProcessorConstructionLimitationException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanPostProcessorDefinitionFactoryConstructorLimitationTest {

    @SneakyThrows
    @DisplayName("Should throw exception when bfpp don't have one constructor and parameter Reflection")
    @Test
    void shouldThrowExceptionWhenBfppHasOneConstructorAndParameterReflection() {
        //given
        var expectedMessage = "BeanProcessor 'NoConstructorWithReflectionParameterBeanFactoryPostProcessor' should have only default constructor without params";

        Executable executable = () -> {
            //when
            BringApplication.run("testdata.bfpp");
        };

        // then
        var noSuchBeanException = assertThrows(BeanPostProcessorConstructionLimitationException.class, executable);
        assertThat(noSuchBeanException.getMessage()).isEqualTo(expectedMessage);

    }
}
