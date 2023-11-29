package testdata.postconstruct.positive;

import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.core.annotation.PostConstruct;
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
