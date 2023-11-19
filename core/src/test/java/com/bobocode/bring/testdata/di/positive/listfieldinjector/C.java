package com.bobocode.bring.testdata.di.positive.listfieldinjector;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.anotation.Order;

@Order(2)
@Component
public class C implements IA {

    @Override
    public void talk() {
        System.out.println("Talk from C");
    }
}
