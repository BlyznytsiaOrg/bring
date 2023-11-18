package com.bobocode.bring.web.servlet;

import com.bobocode.bring.core.anotation.RequestMapping;
import com.bobocode.bring.web.annotation.GetMapping;
import java.util.concurrent.ThreadLocalRandom;

//@RequestMapping(path = "/example")
public class ExampleServlet extends BaseServlet {

    @GetMapping(path = "/hello")
    public String sayHello() {
        return "Hello";
    }

    @GetMapping(path = "/number")
    public int getRandomNumber() {
        return ThreadLocalRandom.current().nextInt(1000);
    }
}
