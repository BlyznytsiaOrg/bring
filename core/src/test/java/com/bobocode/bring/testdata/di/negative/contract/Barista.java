package com.bobocode.bring.testdata.di.negative.contract;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Service;

@Service
public class Barista {
    private Drink drink;

    @Autowired
    public Barista(Drink drink) {
        this.drink = drink;
    }

    public String prepareDrink() {
        final String[] message = {"Barista is preparing a drink: " + drink};
        //drinks.forEach(drink -> message[0] += drink.make() + " ");

        return message[0];
    }
}
