package testdata.di.positive.constructor;

import com.bobocode.bring.core.annotation.Autowired;
import com.bobocode.bring.core.annotation.Component;
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
