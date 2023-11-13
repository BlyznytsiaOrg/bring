package com.bobocode.bring.testdata.di.positive.contract;

import com.bobocode.bring.core.anotation.Component;
import lombok.ToString;

@ToString
@Component
public class Latte implements Drink {
    @Override
    public String make() {
        return "Making a delicious latte!";
    }
}
