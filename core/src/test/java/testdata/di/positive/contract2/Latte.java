package testdata.di.positive.contract2;

import io.github.blyznytsiaorg.bring.core.annotation.Component;

@Component
public class Latte implements Drink {
    @Override
    public String make() {
        return "Making a delicious latte!";
    }
}
