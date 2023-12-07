package testdata.di.negative.noclassimplementation;

import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Service;

@Service
public class Barista {
    private final Latte drink;

    @Autowired
    public Barista(Latte drink) {
        this.drink = drink;
    }

    public String prepareDrink() {
        return "Barista is preparing a drink: " + drink.make();
    }
}
