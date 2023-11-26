package testdata.requestheader;

import com.bobocode.bring.web.servlet.BringServlet;
import com.bobocode.bring.web.servlet.annotation.GetMapping;
import com.bobocode.bring.web.servlet.annotation.RequestHeader;
import com.bobocode.bring.web.servlet.annotation.RestController;

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
