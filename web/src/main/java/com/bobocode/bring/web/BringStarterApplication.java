package com.bobocode.bring.web;

import com.bobocode.bring.core.context.impl.BringApplicationContext;

public class BringStarterApplication {
    public static BringApplicationContext run(String basePackage) {
        BringApplicationContext bringApplicationContext = new BringApplicationContext(basePackage);

        WebStarter webStarter = new WebStarter();
        webStarter.run(bringApplicationContext);

        return bringApplicationContext;
    }
}
