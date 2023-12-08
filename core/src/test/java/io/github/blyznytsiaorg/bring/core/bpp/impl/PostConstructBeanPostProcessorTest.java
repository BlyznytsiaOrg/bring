package io.github.blyznytsiaorg.bring.core.bpp.impl;

import io.github.blyznytsiaorg.bring.core.BringApplication;
import io.github.blyznytsiaorg.bring.core.exception.PostConstructException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import testdata.postconstruct.positive.CustomPostConstruct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostConstructBeanPostProcessorTest {

    @Test
    void shouldFillMessage_postProcessInitialization() {
        //given
        var bringApplicationContext = BringApplication.run("testdata.postconstruct.positive");

        //when
        var myCustomPostConstruct = bringApplicationContext.getBean(CustomPostConstruct.class);

        //then
        assertThat(myCustomPostConstruct.getMessage()).isEqualTo("Hello!");
    }

    @Test
    void shouldThrowException_postProcessInitialization() {
        //given
        var expectedMessage = "@PostConstruct should be added to method without parameters";
        // when
        Executable executable = () -> BringApplication.run("testdata.postconstruct.negative");

        // then
        PostConstructException postConstructException = assertThrows(PostConstructException.class, executable);
        assertThat(postConstructException.getMessage()).isEqualTo(expectedMessage);
    }
}