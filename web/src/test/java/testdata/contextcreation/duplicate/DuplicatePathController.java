package testdata.contextcreation.duplicate;

import io.github.blyznytsiaorg.bring.web.servlet.BringServlet;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.DeleteMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.GetMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.PostMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RestController;

@RestController
public class DuplicatePathController implements BringServlet {

    @GetMapping(path = "/example/hello")
    public String hello() {
        return "Hello";
    }

    @PostMapping(path = "/example/hello")
    public String post() {
        return "post";
    }

    @DeleteMapping(path = "/example/hello")
    public void delete() {

    }
}
