package testdata.di.positive.constructor;

import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Component;
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
