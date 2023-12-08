package testdata.di.positive.contract;

import io.github.blyznytsiaorg.bring.core.annotation.Component;
import lombok.ToString;

@ToString
@Component
public class Latte implements Drink {
    @Override
    public String make() {
        return "Making a delicious latte!";
    }
}
