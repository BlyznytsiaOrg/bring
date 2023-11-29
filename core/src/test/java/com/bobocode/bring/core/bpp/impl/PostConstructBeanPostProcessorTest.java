package com.bobocode.bring.core.bpp.impl;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.core.exception.PostConstructException;
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
        // when
        Executable executable = () -> {
            //given
            BringApplication.run("testdata.postconstruct.negative");
        };

        // then
        assertThrows(PostConstructException.class, executable);
    }
}