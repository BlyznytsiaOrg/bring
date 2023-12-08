package testdata.di.positive.listconstructorinjector;

import io.github.blyznytsiaorg.bring.core.annotation.Component;

@Component
public class C implements IA {

    @Override
    public void talk() {
        System.out.println("Talk from C");
    }
}
