package com.bobocode.bring.testdata.di.positive.constructor;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Component;
import lombok.ToString;

@ToString
@Component
public class B {

    private final A a;

    @Autowired
    public B(A a) {
        this.a = a;
    }

    public A getA() {
        return a;
    }
}
