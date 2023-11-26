package testdata.contextcreation.requestbodytype;

import com.bobocode.bring.core.anotation.RestController;
import com.bobocode.bring.web.servlet.BringServlet;
import com.bobocode.bring.web.servlet.annotation.PostMapping;
import com.bobocode.bring.web.servlet.annotation.RequestBody;

@RestController
public class UnsupportedRequestBodyTypeController implements BringServlet {
    @PostMapping(path = "/bodyAsLong")
    public Long bodyString(@RequestBody long body) {
        return body;
    }
}
