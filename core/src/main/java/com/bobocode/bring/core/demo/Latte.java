package com.bobocode.bring.core.demo;

import com.bobocode.bring.core.anotation.Component;

@Component
public class Latte implements Drink {
    @Override
    public String make() {
        return "Making a delicious latte!";
    }
}
