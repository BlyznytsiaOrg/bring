package testdata.di.negative.nofieldinterfaceimplementation;

import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Component;
import lombok.ToString;

@ToString
@Component
public class B {

    @Autowired
    private A a;

    public A getA() {
        return a;
    }
}
