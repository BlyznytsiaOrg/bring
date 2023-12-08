package io.github.blyznytsiaorg.bring.core.domain;

import io.github.blyznytsiaorg.bring.core.exception.BeanAnnotationMissingException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BeanTypeEnumTest {

    @SneakyThrows
    @Test
    void findBeanType() {
        //given
        Method test = A.class.getDeclaredMethod("test");
        var expectedMessage = "Unable to create Bean of type=[void], methodName=[test]. Method is not annotated with @Bean annotation";

        Executable executable = () -> {
            //when
            BeanTypeEnum.findBeanType(test);
        };

        // then
        var noSuchBeanException = assertThrows(BeanAnnotationMissingException.class, executable);
        assertThat(noSuchBeanException.getMessage()).isEqualTo(expectedMessage);
    }

    static class A {

        public void test() {

        }

    }

}