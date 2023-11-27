package testdata.di.positive.constructorproperties;

import com.bobocode.bring.core.annotation.Autowired;
import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.core.annotation.Value;
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
