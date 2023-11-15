package testdata;

import com.bobocode.bring.web.annotation.GetMapping;
import com.bobocode.bring.web.annotation.RequestMapping;
import com.bobocode.bring.web.servlet.BaseServlet;

@RequestMapping(path = "/example")
public class ExampleServlet extends BaseServlet {

    @GetMapping(path = "/hello")
    public String sayHello() {
        return "Hello";
    }

    @GetMapping(path = "/number")
    public int getNumber() {
        return 200;
    }

}
