package testdata.di.negative.fieldproperties;

import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.core.annotation.Value;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class NotFoundValue {

    @Value("bring.banner-mode")
    private String banner;
}
