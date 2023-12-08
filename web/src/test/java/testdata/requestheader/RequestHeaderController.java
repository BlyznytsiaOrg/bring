package testdata.requestheader;

import io.github.blyznytsiaorg.bring.web.servlet.BringServlet;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.GetMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.PostMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RequestHeader;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RestController;
import io.github.blyznytsiaorg.bring.web.servlet.http.HttpHeaders;
import io.github.blyznytsiaorg.bring.web.servlet.http.HttpStatus;
import io.github.blyznytsiaorg.bring.web.servlet.http.ResponseEntity;

@RestController
public class RequestHeaderController implements BringServlet {

    @GetMapping(path = "/header")
    public String header(@RequestHeader(value = "Content-Type") String header) {
        return header;
    }

    @PostMapping(path = "/header-content-length")
    public Integer headerContentLength(@RequestHeader(value = "Content-Length") Integer header) {
        return header;
    }

    @GetMapping(path = "/headerNonValid")
    public String nonValidHeader(@RequestHeader(value = "Unknown") String header) {
        return header;
    }

    @GetMapping(path = "/custom")
    public ResponseEntity<Void> customHeader(@RequestHeader(value = "Custom") String header) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Custom", header);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

}
