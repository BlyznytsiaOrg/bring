package testdata.di.positive.contract2;

import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Service;
import lombok.Getter;

@Getter
@Service
public class Barista {
    private final Drink drink;

    @Autowired
    public Barista(Drink latte) {
        this.drink = latte;
    }

    public String prepareDrink() {
        final String[] message = {"Barista is preparing a drink: " + drink.make()};
        //drinks.forEach(drink -> message[0] += drink.make() + " ");

        return message[0];
    }
}
