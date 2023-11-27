package testdata.di.negative.cyclic;

import com.bobocode.bring.core.annotation.Autowired;
import com.bobocode.bring.core.annotation.Component;

@Component
public class B {

    private final A a;

    @Autowired
    public B(A a) {
        this.a = a;
    }
}
