package testdata.di.negative.settercyclic;

import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Component;

@Component
public class C {

    private B b;

    @Autowired
    public void setB(B b) {
        this.b = b;
    }
}
