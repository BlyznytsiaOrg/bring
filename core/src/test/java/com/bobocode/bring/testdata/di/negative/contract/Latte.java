package com.bobocode.bring.testdata.di.negative.contract;

import com.bobocode.bring.core.anotation.Component;

@Component
public class Latte implements Drink {
    @Override
    public String make() {
        return "Making a delicious latte!";
    }
}
