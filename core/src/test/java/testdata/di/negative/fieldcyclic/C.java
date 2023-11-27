package testdata.di.negative.fieldcyclic;

import com.bobocode.bring.core.annotation.Autowired;
import com.bobocode.bring.core.annotation.Component;

@Component
public class C {

    @Autowired
    private B b;
}
