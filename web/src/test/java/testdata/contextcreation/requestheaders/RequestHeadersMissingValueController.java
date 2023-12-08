package testdata.contextcreation.requestheaders;

import io.github.blyznytsiaorg.bring.web.servlet.BringServlet;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.GetMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RequestHeader;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RestController;

@RestController
public class RequestHeadersMissingValueController implements BringServlet {
    @GetMapping(path = "/header")
    public String getHeader(@RequestHeader String header) {
        return header;
    }
}
