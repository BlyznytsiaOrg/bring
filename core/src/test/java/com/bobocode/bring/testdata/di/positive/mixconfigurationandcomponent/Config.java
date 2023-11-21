package com.bobocode.bring.testdata.di.positive.mixconfigurationandcomponent;

import com.bobocode.bring.core.anotation.Bean;
import com.bobocode.bring.core.anotation.Configuration;

@Configuration
public class Config {

    @Bean
    public String name() {
        return "Bob";
    }
}
