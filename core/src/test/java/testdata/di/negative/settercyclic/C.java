package testdata.di.negative.settercyclic;

import com.bobocode.bring.core.annotation.Autowired;
import com.bobocode.bring.core.annotation.Component;

@Component
public class C {

    private B b;

    @Autowired
    public void setB(B b) {
        this.b = b;
    }
}
