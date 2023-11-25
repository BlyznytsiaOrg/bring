package testdata.requestHeader;

import com.bobocode.bring.core.anotation.RestController;
import com.bobocode.bring.web.servlet.BringServlet;
import com.bobocode.bring.web.servlet.annotation.GetMapping;
import com.bobocode.bring.web.servlet.annotation.RequestHeader;

@RestController
public class RequestHeaderController implements BringServlet {

    @GetMapping(path = "/header")
    public String header(@RequestHeader(value = "Content-Type") String header) {
        return header;
    }

    @GetMapping(path = "/headerNonValid")
    public String nonValidHeader(@RequestHeader(value = "Unknown") String header) {
        return header;
    }

    @GetMapping(path = "/custom")
    public String customHeader(@RequestHeader(value = "Custom") String header) {
        return header;
    }

}
