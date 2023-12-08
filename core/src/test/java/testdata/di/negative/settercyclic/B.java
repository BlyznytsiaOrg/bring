package testdata.di.negative.settercyclic;

import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Component;

@Component
public class B {
    private A a;

    @Autowired
    public void setA(A a) {
        this.a = a;
    }

    public A getA() {
        return a;
    }
}
