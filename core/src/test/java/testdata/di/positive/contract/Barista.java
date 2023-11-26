package testdata.di.positive.contract;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Service;

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
