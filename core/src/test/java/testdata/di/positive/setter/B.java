package testdata.di.positive.setter;

import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Component;
import lombok.ToString;

@ToString
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
