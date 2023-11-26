package testdata.di.negative.noimplementationviasetter;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Component;
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
