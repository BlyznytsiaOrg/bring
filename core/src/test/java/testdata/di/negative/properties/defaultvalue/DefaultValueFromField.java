package testdata.di.negative.properties.defaultvalue;

import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.core.annotation.Value;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class DefaultValueFromField {

    @Value("bring.banner-mode.field:defaultValue")
    private String banner;
}
