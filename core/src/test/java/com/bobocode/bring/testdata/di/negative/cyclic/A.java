package com.bobocode.bring.testdata.di.negative.cyclic;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Component;

@Component
public class A {

    private final B b;

    @Autowired
    public A(B b) {
        this.b = b;
    }
}
