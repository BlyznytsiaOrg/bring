package com.bobocode.bring.web.servlet;

import com.bobocode.bring.core.anotation.RequestMapping;
import com.bobocode.bring.web.annotation.GetMapping;
import com.bobocode.bring.web.annotation.PathVariable;
import com.bobocode.bring.web.annotation.RequestMapping;
import com.bobocode.bring.web.annotation.RestController;

@RestController
@RequestMapping(path = "/example")
public class ExampleRestController extends BaseServlet {

    @GetMapping(path = "/hello")
    public String sayHello() {
        return "Hello";
    }

    @GetMapping(path = "/number")
    public int getRandomNumber() {
        return 200;
    }

    @GetMapping(path = "/{id}")
    public StringBuilder variable(@PathVariable StringBuilder id) {
        return id;
    }
}
