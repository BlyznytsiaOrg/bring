package testdata.controller;

import com.bobocode.bring.core.anotation.RequestMapping;
import com.bobocode.bring.web.annotation.GetMapping;
import com.bobocode.bring.web.servlet.BaseServlet;
import testdata.exception.TestCustomException;

@RequestMapping(path = "/example")
public class ExampleServlet extends BaseServlet {

    @GetMapping(path = "/hello")
    public String sayHello() {
        return "Hello";
    }

    @GetMapping(path = "/number")
    public int getNumber() {
        return 200;
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
