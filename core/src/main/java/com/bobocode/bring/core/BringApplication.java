package com.bobocode.bring.core;

import com.bobocode.bring.core.context.impl.BringApplicationContext;

public class BringApplication {

    public static void main(String[] args) {
        BringApplicationContext bringApplicationContext = new BringApplicationContext(BringApplication.class);
    }
}
