package testdata.contextcreation.duplicate;

import com.bobocode.bring.web.servlet.BringServlet;
import com.bobocode.bring.web.servlet.annotation.DeleteMapping;
import com.bobocode.bring.web.servlet.annotation.GetMapping;
import com.bobocode.bring.web.servlet.annotation.PostMapping;
import com.bobocode.bring.web.servlet.annotation.RestController;

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
