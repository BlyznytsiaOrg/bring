package testdata.di.negative.noimplementation;

import com.bobocode.bring.core.annotation.Autowired;
import com.bobocode.bring.core.annotation.Service;

@Service
public class Barista {
    private final Drink drink;

    @Autowired
    public Barista(Drink drink) {
        this.drink = drink;
    }

    public String prepareDrink() {
        return "Barista is preparing a drink: " + drink.make();
    }
}
