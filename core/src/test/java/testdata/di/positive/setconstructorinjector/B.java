package testdata.di.positive.setconstructorinjector;

import com.bobocode.bring.core.annotation.Component;


@Component
public class B implements IA {
    @Override
    public void talk() {
        System.out.println("Talk from B");
    }
}
