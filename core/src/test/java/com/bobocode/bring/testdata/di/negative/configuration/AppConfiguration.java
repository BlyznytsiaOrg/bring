package com.bobocode.bring.testdata.di.negative.configuration;

import com.bobocode.bring.core.anotation.Bean;
import com.bobocode.bring.core.anotation.Configuration;

@Configuration
public class AppConfiguration {
    
    @Bean
    public A a() {
        return A.builder().field("Hello!").build();
    }
    
    @Bean
    public B b(A aaaa) {
        return B.builder().a(aaaa).build();
    }
    
}
