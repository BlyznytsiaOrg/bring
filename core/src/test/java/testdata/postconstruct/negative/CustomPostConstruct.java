package testdata.postconstruct.negative;

import io.github.blyznytsiaorg.bring.core.annotation.Component;
import io.github.blyznytsiaorg.bring.core.annotation.PostConstruct;
import lombok.Getter;

@Getter
@Component
public class CustomPostConstruct {

    private String message;

    @PostConstruct
    public void fillMessage(String invalidParam) {
        message = "Hello!";
    }
}
