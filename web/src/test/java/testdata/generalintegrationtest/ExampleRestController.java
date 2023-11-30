package testdata.generalintegrationtest;

import com.bobocode.bring.web.servlet.BringServlet;
import com.bobocode.bring.web.servlet.annotation.GetMapping;
import com.bobocode.bring.web.servlet.annotation.PathVariable;
import com.bobocode.bring.web.servlet.annotation.PostMapping;
import com.bobocode.bring.web.servlet.annotation.PutMapping;
import com.bobocode.bring.web.servlet.annotation.RequestBody;
import com.bobocode.bring.web.servlet.annotation.RequestHeader;
import com.bobocode.bring.web.servlet.annotation.RequestMapping;
import com.bobocode.bring.web.servlet.annotation.RequestParam;
import com.bobocode.bring.web.servlet.annotation.ResponseStatus;
import com.bobocode.bring.web.servlet.annotation.RestController;
import com.bobocode.bring.web.servlet.http.HttpHeaders;
import com.bobocode.bring.web.servlet.http.HttpStatus;
import com.bobocode.bring.web.servlet.http.ResponseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import testdata.exception.TestCustomException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/example")
public class ExampleRestController implements BringServlet {

    @GetMapping(path = "/hello")
    public String sayHello() {
        return "Hello";
    }

    @GetMapping(path = "/number")
    public int getNumber() {
        return 200;
    }

    @GetMapping(path = "/custom-exception")
    public void throwCustomException() {
        throw new TestCustomException("TestCustomException");
    }

    @GetMapping(path = "/default-exception")
    public void throwDefaultException() {
        throw new RuntimeException("TestDefaultException");
    }

    @GetMapping(path = "/reqParam")
    public String reqParam(@RequestParam String name, @RequestParam Long id) {
        return name + " - " + id;
    }

    @PutMapping(path = "/request")
    public int request(HttpServletRequest request) {
        return request.getContentLength();
    }

    @GetMapping(path = "/response")
    public String response(HttpServletResponse response) {
        response.setContentType("application/json");
        return response.getContentType();
    }

    @PostMapping(path = "/bodyAsString")
    public ResponseEntity<String> bodyString(@RequestBody String body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping(path = "/bodyAsEntity")
    public User bodyEntity(@RequestBody User user) {
        return user;
    }

    @GetMapping(path = "/header")
    public String header(@RequestHeader(value = "Content-Type") String header) {
        return header;
    }

    @GetMapping(path = "/status")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public String status() {
       return "";
    }

    @PostMapping(path = "/severalArg/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> several(@PathVariable Long id,
                          @RequestHeader(value = "Accept") String header,
                          @RequestBody User user,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        String contentType = request.getContentType();
        int status = response.getStatus();
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("id", id);
        objectMap.put("Accept", header);
        objectMap.put("user", user);
        objectMap.put("Content-Type", contentType);
        objectMap.put("status", status);
        return new ResponseEntity<>(objectMap, HttpStatus.OK);
    }


    public record User(String name, int age) {
    }
}

