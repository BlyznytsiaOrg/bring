package testdata.requestbody;

import com.bobocode.bring.core.anotation.RestController;
import com.bobocode.bring.web.servlet.BringServlet;
import com.bobocode.bring.web.servlet.annotation.PostMapping;
import com.bobocode.bring.web.servlet.annotation.RequestBody;
import java.util.Map;

@RestController
public class RequestBodyController implements BringServlet {

    @PostMapping(path = "/bodyAsString")
    public String bodyString(@RequestBody String body) {
        return body;
    }

    @PostMapping(path = "/bodyAsEntity")
    public User bodyEntity(@RequestBody User user) {
        return user;
    }

    @PostMapping(path = "/bodyAsMap")
    public Map<String, Object> bodyAsMap(@RequestBody Map<String, Object> value) {
        return value;
    }

    @PostMapping(path = "/bodyAsByteArray")
    public byte[] bodyAsByteArray(@RequestBody byte[] data) {
        return data;
    }

    public record User(String name, int age) {}
}
