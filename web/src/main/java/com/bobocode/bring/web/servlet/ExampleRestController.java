package com.bobocode.bring.web.servlet;

import com.bobocode.bring.core.anotation.RestController;
import com.bobocode.bring.web.annotation.GetMapping;
import com.bobocode.bring.web.annotation.PathVariable;
import com.bobocode.bring.web.annotation.RequestMapping;
import com.bobocode.bring.web.annotation.RequestParam;

//TODO need to delete them later
//@RestController
//@RequestMapping(path = "/example")
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
    public String variable(@PathVariable String id) {
        return id;
    }

    @GetMapping(path = "/reqParam")
    public String reqMapping(@RequestParam String name, @RequestParam Long id) {
        return name + " - " + id;
    }
}
