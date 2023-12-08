package testdata.mixconfigurationandrestcontroller;

import io.github.blyznytsiaorg.bring.web.servlet.BringServlet;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.PostMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RequestBody;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RequestMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import testdata.mixconfigurationandrestcontroller.dto.UserRequest;
import testdata.mixconfigurationandrestcontroller.dto.UserResponse;

@RequestMapping(path = "/api")
@RestController
public class ShortenController implements BringServlet {

    private final ObjectMapper objectMapper;

    public ShortenController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @PostMapping(path = "/shorten")
    public String shorten(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = new UserResponse();
        userResponse.setShortenedUrl(userRequest.getOriginalUrl());
        return objectMapper.writeValueAsString(userResponse);
    }
}


