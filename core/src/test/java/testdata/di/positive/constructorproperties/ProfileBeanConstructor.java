package testdata.di.positive.constructorproperties;

import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Component;
import io.github.blyznytsiaorg.bring.core.annotation.Value;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class ProfileBeanConstructor {

    private final String bannerMode;

    @Autowired
    public ProfileBeanConstructor(@Value("bring.main.banner-mode") String bannerMode) {
        this.bannerMode = bannerMode;
    }
}
