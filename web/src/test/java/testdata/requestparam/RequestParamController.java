package testdata.requestparam;

import io.github.blyznytsiaorg.bring.web.servlet.BringServlet;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.GetMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RequestParam;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RestController;

@RestController
public class RequestParamController implements BringServlet {

    @GetMapping(path = "/str")
    public String reqParamString(@RequestParam String name) {
        return name;
    }

    @GetMapping(path = "/long")
    public Long reqParamLong(@RequestParam Long id) {
        return id;
    }

    @GetMapping
    public String reqParam(@RequestParam String name, @RequestParam Long id) {
        return name + " - " + id;
    }
}
