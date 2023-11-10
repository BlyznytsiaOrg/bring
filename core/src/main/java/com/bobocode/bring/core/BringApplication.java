package com.bobocode.bring.core;

import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.core.demo.*;

public class BringApplication {

    public static void main(String[] args) {
        BringApplicationContext context = new BringApplicationContext(BringApplication.class);

        Barista barista = context.getBean(Barista.class);
        System.out.println(barista.prepareDrink());
    }
}
