package testdata.di.negative.fieldcyclic;

import com.bobocode.bring.core.annotation.Autowired;
import com.bobocode.bring.core.annotation.Component;

@Component
public class B {
    @Autowired
    private A a;

    public A getA() {
        return a;
    }
}
