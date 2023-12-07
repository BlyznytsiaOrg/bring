package testdata.contextcreation.duplicate;

import io.github.blyznytsiaorg.bring.web.servlet.BringServlet;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.GetMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.PathVariable;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.PutMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RequestMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RestController;

@RestController
@RequestMapping(path = "/example")
public class DuplicatePathController2 implements BringServlet {

    @GetMapping(path = "/hello")
    public String hello() {
        return "Hello";
    }

    @PutMapping(path = "/put")
    public String put1() {
        return "put1";
    }

    @PutMapping(path = "/put")
    public String put2(@PathVariable String name) {
        return name;
    }
}
