package testdata.exception;

import io.github.blyznytsiaorg.bring.web.servlet.annotation.ResponseStatus;
import io.github.blyznytsiaorg.bring.web.servlet.http.HttpStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request")
public class TestCustomException extends RuntimeException {

    public TestCustomException(String message) {
        super(message);
    }
}
