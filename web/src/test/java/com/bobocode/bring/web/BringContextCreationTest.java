package com.bobocode.bring.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.bobocode.bring.web.servlet.exception.MissingBringServletImplException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BringContextCreationTest {
    public static final String PACKAGE = "testdata.contextcreation.nobringservlet";

    @Test
    @DisplayName("should throw MissingBringServletImplException when RestController does not "
            + "implements BringServlet")
    void shouldThrowRequestPathDuplicateException() {
        String expectedErrorMessage = "RestController 'NoBringServletImpl' should implement "
                + "BringServlet interface";

        //when
        RuntimeException exception = Assertions.catchRuntimeException(
                () -> BringWebApplication.run(PACKAGE));
        String actualMessage = exception.getMessage();

        //then
        assertThat(exception).isExactlyInstanceOf(MissingBringServletImplException.class);
        assertThat(actualMessage).isEqualTo(expectedErrorMessage);
    }
}
