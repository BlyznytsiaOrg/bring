package testdata.di.negative.secondconstructor;

import io.github.blyznytsiaorg.bring.core.annotation.Component;
import lombok.ToString;

@ToString
@Component
public class B {

    private A a;

    public B() {
    }

    public B(A a) {
        this.a = a;
    }

    public A getA() {
        return a;
    }
}
