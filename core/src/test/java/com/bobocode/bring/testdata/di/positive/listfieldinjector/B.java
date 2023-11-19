package com.bobocode.bring.testdata.di.positive.listfieldinjector;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.anotation.Order;
@Order(1)
@Component
public class B implements IA {
    @Override
    public void talk() {
        System.out.println("Talk from B");
    }
}
