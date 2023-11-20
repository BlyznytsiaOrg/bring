package testdata.controller;

import com.bobocode.bring.core.anotation.RestController;
import com.bobocode.bring.web.servlet.annotation.GetMapping;
import com.bobocode.bring.web.servlet.annotation.PathVariable;
import com.bobocode.bring.web.servlet.annotation.PostMapping;
import com.bobocode.bring.web.servlet.annotation.PutMapping;
import com.bobocode.bring.web.servlet.annotation.RequestBody;
import com.bobocode.bring.web.servlet.annotation.RequestMapping;
import com.bobocode.bring.web.servlet.annotation.RequestParam;
import com.bobocode.bring.web.servlet.BaseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @GetMapping(path = "/reqParam")
    public String reqParam(@RequestParam String name, @RequestParam Long id) {
        return name + " - " + id;
    }

    @PutMapping(path = "/request")
    public int request(HttpServletRequest request) {
        return request.getContentLength();
    }

    @GetMapping(path = "/response")
    public String response(HttpServletResponse response) {
        response.setContentType("application/json");
        return response.getContentType();
    }

    @PostMapping(path = "/bodyAsString")
    public String bodyString(@RequestBody String body) {
        return body;
    }

    @PostMapping(path = "/bodyAsEntity")
    public User bodyEntity(@RequestBody User user) {
        return user;
    }


    public record User(String name, int age) {
    }
}

