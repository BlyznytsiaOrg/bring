package testdata.postconstruct.negative;

import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.core.annotation.PostConstruct;
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
