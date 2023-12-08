package testdata.contextcreation.requestbodytype;

import io.github.blyznytsiaorg.bring.web.servlet.BringServlet;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.PostMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RequestBody;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RestController;

@RestController
public class UnsupportedRequestBodyTypeController implements BringServlet {
    @PostMapping(path = "/bodyAsLong")
    public Long bodyString(@RequestBody long body) {
        return body;
    }
}
