package testdata.di.positive.listfieldinjector;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.anotation.Order;

@Component
@Order(5)
public class D implements IA {
    @Override
    public void talk() {
        System.out.println("Hello from D!");
    }
}
