package testdata.di.negative.oneinterfacetwodependency;

import io.github.blyznytsiaorg.bring.core.annotation.Component;

@Component
public class Espresso implements Drink {
    @Override
    public String make() {
        return "Brewing a strong espresso!";
    }
}
