package testdata.di.positive.listconstructorinjector;

import com.bobocode.bring.core.anotation.Component;

@Component
public class C implements IA {

    @Override
    public void talk() {
        System.out.println("Talk from C");
    }
}