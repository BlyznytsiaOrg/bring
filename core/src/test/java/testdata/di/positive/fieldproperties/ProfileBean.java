package testdata.di.positive.fieldproperties;

import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.core.annotation.Profile;
import com.bobocode.bring.core.annotation.Value;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Profile("dev")
@Component
public class ProfileBean {

    @Value("bring.main.banner-mode")
    private String bannerMode;
}
