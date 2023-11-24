package testdata.di.positive.listfieldinjector;

import com.bobocode.bring.core.anotation.Component;

@Component
public class C implements IA {

    @Override
    public void talk() {
        System.out.println("Talk from C");
    }
}
