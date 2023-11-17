package com.bobocode.bring.testdata.di.positive.setterproperties;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.anotation.Profile;
import com.bobocode.bring.core.anotation.Value;
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
