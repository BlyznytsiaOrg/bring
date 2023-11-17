package com.bobocode.bring.testdata.di.positive.fieldproperties;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.anotation.Profile;
import com.bobocode.bring.core.anotation.Value;
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
