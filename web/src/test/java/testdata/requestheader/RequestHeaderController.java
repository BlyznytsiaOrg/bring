package testdata.requestheader;

import com.bobocode.bring.web.servlet.BringServlet;
import com.bobocode.bring.web.servlet.annotation.GetMapping;
import com.bobocode.bring.web.servlet.annotation.PostMapping;
import com.bobocode.bring.web.servlet.annotation.RequestHeader;
import com.bobocode.bring.web.servlet.annotation.RestController;
import com.bobocode.bring.web.servlet.http.HttpHeaders;
import com.bobocode.bring.web.servlet.http.HttpStatus;
import com.bobocode.bring.web.servlet.http.ResponseEntity;

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
