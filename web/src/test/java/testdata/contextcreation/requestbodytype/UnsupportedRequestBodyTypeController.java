package testdata.contextcreation.requestbodytype;

import com.bobocode.bring.web.servlet.BringServlet;
import com.bobocode.bring.web.servlet.annotation.PostMapping;
import com.bobocode.bring.web.servlet.annotation.RequestBody;
import com.bobocode.bring.web.servlet.annotation.RestController;

@RestController
public class UnsupportedRequestBodyTypeController implements BringServlet {
    @PostMapping(path = "/bodyAsLong")
    public Long bodyString(@RequestBody long body) {
        return body;
    }
}
