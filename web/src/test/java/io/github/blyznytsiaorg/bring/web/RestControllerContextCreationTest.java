package io.github.blyznytsiaorg.bring.web;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blyznytsiaorg.bring.web.servlet.exception.MissingRequestHeaderAnnotationValueException;
import io.github.blyznytsiaorg.bring.web.servlet.exception.RequestBodyTypeUnsupportedException;
import io.github.blyznytsiaorg.bring.web.servlet.exception.RequestPathDuplicateException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestControllerContextCreationTest {

    public static final String PACKAGE_DUPLICATE = "testdata.contextcreation.duplicate";
    public static final String PACKAGE_REQUEST_BODY = "testdata.contextcreation.requestbodytype";
    public static final String PACKAGE_REQUEST_HEADERS = "testdata.contextcreation.requestheaders";

    @Test
    @DisplayName("should throw RequestPathDuplicateException with message")
    void shouldThrowRequestPathDuplicateException() {
        String expectedErrorMessage = "Error on duplicate path: { GET [/example/hello] }, "
                + "{ PUT [/example/put] }";

        //when
        RuntimeException exception = Assertions.catchRuntimeException(
                () -> BringWebApplication.run(PACKAGE_DUPLICATE));
        String actualMessage = exception.getMessage();

        //then
        assertThat(exception).isExactlyInstanceOf(RequestPathDuplicateException.class);
        assertThat(actualMessage).isEqualTo(expectedErrorMessage);
    }

    @Test
    @DisplayName("should throw RequestBodyTypeUnsupportedException with message")
    void shouldThrowRequestBodyTypeUnsupportedException() {
        String expectedErrorMessage = "Invalid type 'long' for parameter 'body' "
                + "annotated with @RequestBody of method 'bodyString'";

        //when
        RuntimeException exception = Assertions.catchRuntimeException(
                () -> BringWebApplication.run(PACKAGE_REQUEST_BODY));
        String actualMessage = exception.getMessage();

        //then
        assertThat(exception).isExactlyInstanceOf(RequestBodyTypeUnsupportedException.class);
        assertThat(actualMessage).isEqualTo(expectedErrorMessage);
    }

    @Test
    @DisplayName("should throw MissingRequestHeaderAnnotationValueException with message")
    void shouldThrowMissingRequestHeaderAnnotationValueException() {
        String expectedErrorMessage = "Required value for @RequestHeader annotation for parameter "
                + "'header' of method 'getHeader' is not present";

        //when
        RuntimeException exception = Assertions.catchRuntimeException(
                () -> BringWebApplication.run(PACKAGE_REQUEST_HEADERS));
        String actualMessage = exception.getMessage();

        //then
        assertThat(exception).isExactlyInstanceOf(MissingRequestHeaderAnnotationValueException.class);
        assertThat(actualMessage).isEqualTo(expectedErrorMessage);
    }
}
