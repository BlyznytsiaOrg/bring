package testdata.di.negative.properties.field;

import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.core.annotation.Value;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class NotFoundValueFromField {

    @Value("bring.banner-mode.field")
    private String banner;
}
