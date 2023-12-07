package testdata.di.positive.fieldproperties;

import io.github.blyznytsiaorg.bring.core.annotation.Component;
import io.github.blyznytsiaorg.bring.core.annotation.Value;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class ProfileBean {

    @Value("bring.main.banner-mode")
    private String bannerMode;
}
