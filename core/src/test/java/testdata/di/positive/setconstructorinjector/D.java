package testdata.di.positive.setconstructorinjector;

import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.core.annotation.Order;
import testdata.di.positive.listfieldinjector.IA;

@Component
@Order(5)
public class D implements IA {
    @Override
    public void talk() {
        System.out.println("Hello from D!");
    }
}
