package testdata.requestparam;

import com.bobocode.bring.web.servlet.BringServlet;
import com.bobocode.bring.web.servlet.annotation.GetMapping;
import com.bobocode.bring.web.servlet.annotation.RequestParam;
import com.bobocode.bring.web.servlet.annotation.RestController;

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
