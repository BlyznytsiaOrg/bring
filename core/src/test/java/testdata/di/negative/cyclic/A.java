package testdata.di.negative.cyclic;

import com.bobocode.bring.core.annotation.Autowired;
import com.bobocode.bring.core.annotation.Component;

@Component
public class A {

    private final B b;

    @Autowired
    public A(B b) {
        this.b = b;
    }
}
