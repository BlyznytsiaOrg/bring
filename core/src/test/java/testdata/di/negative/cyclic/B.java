package testdata.di.negative.cyclic;

import com.bobocode.bring.core.annotation.Component;

@Component
public class B {

    private final A a;

    public B(A a) {
        this.a = a;
    }
}
