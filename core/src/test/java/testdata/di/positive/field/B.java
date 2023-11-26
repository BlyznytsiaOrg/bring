package testdata.di.positive.field;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Component;
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
