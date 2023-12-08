package testdata.di.positive.setsetterinjector;

import io.github.blyznytsiaorg.bring.core.annotation.Component;

@Component
public class B implements IA {
    @Override
    public void talk() {
        System.out.println("Talk from B");
    }
}
