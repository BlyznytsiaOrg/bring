package testdata.di.negative.properties.field;

import io.github.blyznytsiaorg.bring.core.annotation.Component;
import io.github.blyznytsiaorg.bring.core.annotation.Value;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class NotFoundValueFromField {

    @Value("bring.banner-mode.field")
    private String banner;
}
