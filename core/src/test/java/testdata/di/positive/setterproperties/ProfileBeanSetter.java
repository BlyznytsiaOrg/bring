package testdata.di.positive.setterproperties;

import com.bobocode.bring.core.annotation.Autowired;
import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.core.annotation.Profile;
import com.bobocode.bring.core.annotation.Value;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Profile("dev")
@Component
public class ProfileBeanSetter {

    private String bannerMode;

    @Autowired
    public void setBannerMode(@Value("bring.main.banner-mode") String bannerMode) {
        this.bannerMode = bannerMode;
    }
}
