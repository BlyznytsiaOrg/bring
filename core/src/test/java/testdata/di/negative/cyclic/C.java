package testdata.di.negative.cyclic;

import io.github.blyznytsiaorg.bring.core.annotation.Component;

@Component
public class C {

    private final B b;

    public C(B b) {
        this.b = b;
    }
}
