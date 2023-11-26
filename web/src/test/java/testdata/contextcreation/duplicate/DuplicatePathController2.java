package testdata.contextcreation.duplicate;

import com.bobocode.bring.core.anotation.RestController;
import com.bobocode.bring.web.servlet.BringServlet;
import com.bobocode.bring.web.servlet.annotation.GetMapping;
import com.bobocode.bring.web.servlet.annotation.PathVariable;
import com.bobocode.bring.web.servlet.annotation.PutMapping;
import com.bobocode.bring.web.servlet.annotation.RequestMapping;

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
