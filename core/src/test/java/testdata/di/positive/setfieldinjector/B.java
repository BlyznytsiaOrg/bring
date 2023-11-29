package testdata.di.positive.setfieldinjector;

import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.core.annotation.Order;

@Order(-1)
@Component
public class B implements IA {
    @Override
    public void talk() {
        System.out.println("Talk from B");
    }
}
