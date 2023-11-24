package testdata.di.positive.secondconstructor;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Component;
import lombok.ToString;

@ToString
@Component
public class B {

    private A a;

    public B() {
    }

    @Autowired
    public B(A a) {
        this.a = a;
    }

    public A getA() {
        return a;
    }
}
