package testdata.di.negative.settercyclic;

import com.bobocode.bring.core.annotation.Autowired;
import com.bobocode.bring.core.annotation.Component;

@Component
public class A {

    private C c;

    @Autowired
    public void setC(C c) {
        this.c = c;
    }
}
