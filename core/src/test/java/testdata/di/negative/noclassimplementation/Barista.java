package testdata.di.negative.noclassimplementation;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Service;

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
