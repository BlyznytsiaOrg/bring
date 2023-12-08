package testdata.postconstruct.positive;

import io.github.blyznytsiaorg.bring.core.annotation.Component;
import io.github.blyznytsiaorg.bring.core.annotation.PostConstruct;
import lombok.Getter;

@Component
@Getter
public class CustomPostConstruct {

    private String message;

    @PostConstruct
    public void fillMessage() {
        message = "Hello!";
    }
}
