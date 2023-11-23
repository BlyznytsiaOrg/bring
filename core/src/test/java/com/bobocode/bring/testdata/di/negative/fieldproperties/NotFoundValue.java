package com.bobocode.bring.testdata.di.negative.fieldproperties;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.anotation.Value;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class NotFoundValue {

    @Value("bring.banner-mode")
    private String banner;
}
