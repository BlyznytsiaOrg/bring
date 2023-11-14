package com.bobocode.bring.web.servlet;

import com.bobocode.bring.web.annotation.GetMapping;
import com.bobocode.bring.web.annotation.RequestMapping;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RequestMapping(path = "/example")
public class ExampleServlet extends BaseServlet {

    @GetMapping(path = "/hello")
    public String sayHello() {
        return "Hello";
    }

    @GetMapping(path = "/number")
    public int getRandomNumber() {
        return ThreadLocalRandom.current().nextInt(1000);
    }

    @GetMapping(path = "/headers")
    public Map<String, String> getHeaders() {
        return getRequestHeaders().getHeaders();
    }

}
