package testdata.di.negative.settercyclic;

import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Component;

@Component
public class A {

    private C c;

    @Autowired
    public void setC(C c) {
        this.c = c;
    }
}
