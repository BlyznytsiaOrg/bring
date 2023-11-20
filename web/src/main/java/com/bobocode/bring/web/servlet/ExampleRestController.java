package com.bobocode.bring.web.servlet;

import com.bobocode.bring.core.anotation.RestController;
import com.bobocode.bring.web.servlet.annotation.GetMapping;
import com.bobocode.bring.web.servlet.annotation.PathVariable;
import com.bobocode.bring.web.servlet.annotation.PostMapping;
import com.bobocode.bring.web.servlet.annotation.RequestBody;
import com.bobocode.bring.web.servlet.annotation.RequestMapping;
import com.bobocode.bring.web.servlet.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;

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

    @PostMapping(path = "/reqBody")
    public UserDto reqBody(@RequestBody UserDto body) {
        return body;
    }

    @GetMapping(path = "/request")
    public String response(HttpServletRequest request) {
        return request.getClass().getSimpleName();
    }

    public record UserDto(String name, int age) {
    }

}
