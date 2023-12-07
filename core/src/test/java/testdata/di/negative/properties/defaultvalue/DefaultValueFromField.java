package testdata.di.negative.properties.defaultvalue;

import io.github.blyznytsiaorg.bring.core.annotation.Component;
import io.github.blyznytsiaorg.bring.core.annotation.Value;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class DefaultValueFromField {

    @Value("bring.banner-mode.field:defaultValue")
    private String banner;
}
