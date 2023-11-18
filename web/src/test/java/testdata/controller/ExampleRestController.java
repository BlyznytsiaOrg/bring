package testdata.controller;

import com.bobocode.bring.core.anotation.RestController;
import com.bobocode.bring.web.annotation.GetMapping;
import com.bobocode.bring.web.annotation.PathVariable;
import com.bobocode.bring.web.annotation.RequestMapping;
import com.bobocode.bring.web.servlet.BaseServlet;
import testdata.exception.TestCustomException;

@RestController
@RequestMapping(path = "/example")
public class ExampleRestController extends BaseServlet {

    @GetMapping(path = "/hello")
    public String sayHello() {
        return "Hello";
    }

    @GetMapping(path = "/number")
    public int getNumber() {
        return 200;
    }

    @GetMapping(path = "/{id}")
    public Long getPathVariableLong(@PathVariable Long id) {
        return id;
    }

    @GetMapping(path = "/variable1/{value}")
    public boolean getPathVariableBoolean(@PathVariable boolean value) {
        return value;
    }

    @GetMapping(path = "/custom-exception")
    public void throwCustomException() {
        throw new TestCustomException("TestCustomException");
    }

    @GetMapping(path = "/default-exception")
    public void throwDefaultException() {
        throw new RuntimeException("TestDefaultException");
    }

}
