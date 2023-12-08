package testdata.di.positive.setconstructorinjector;

import io.github.blyznytsiaorg.bring.core.annotation.Component;
import io.github.blyznytsiaorg.bring.core.annotation.Order;

@Component
@Order(5)
public class D implements IA {
    @Override
    public void talk() {
        System.out.println("Hello from D!");
    }
}
