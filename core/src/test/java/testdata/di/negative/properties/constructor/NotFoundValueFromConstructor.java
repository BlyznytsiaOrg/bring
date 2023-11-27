package testdata.di.negative.properties.constructor;

import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.core.annotation.Value;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class NotFoundValueFromConstructor {

    private String banner;

    public NotFoundValueFromConstructor(@Value("bring.banner-mode.constructor") String banner) {
        this.banner = banner;
    }
}
