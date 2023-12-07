package testdata.di.positive.contract2;

import io.github.blyznytsiaorg.bring.core.annotation.Component;

@Component
public class Espresso implements Drink {
    @Override
    public String make() {
        return "Brewing a strong espresso!";
    }
}
