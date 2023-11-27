package testdata.di.negative.cyclic;

import com.bobocode.bring.core.annotation.Component;

@Component
public class A {

    private final C c;

    public A(C c) {
        this.c = c;
    }
}
