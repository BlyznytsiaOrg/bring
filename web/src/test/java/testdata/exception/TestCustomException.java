package testdata.exception;

import com.bobocode.bring.web.servlet.annotation.ResponseStatus;
import com.bobocode.bring.web.servlet.http.HttpStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request")
public class TestCustomException extends RuntimeException {

    public TestCustomException(String message) {
        super(message);
    }
}
