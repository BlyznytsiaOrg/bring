package testdata.di.negative.fieldcyclic;

import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Component;

@Component
public class A {

    @Autowired
    private C c;

}
