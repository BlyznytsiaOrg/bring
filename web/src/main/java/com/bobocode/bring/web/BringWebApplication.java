package com.bobocode.bring.web;

import com.bobocode.bring.core.context.impl.BringApplicationContext;


public class BringWebApplication {
    public static BringApplicationContext run(String basePackage) {
        // Create context: register Bean definitions
        BringApplicationContext context = new BringApplicationContext(basePackage);

        // Invoke Bean Post Processors, create Bean objects
        context.refresh();

        var webStarter = context.getBean(WebStarter.class);
        webStarter.run(context);

        return context;
    }

    public static BringApplicationContext run(Class<?> clazz) {
        // Create context: register Bean definitions
        BringApplicationContext context = new BringApplicationContext(clazz);

        // Invoke Bean Post Processors, create Bean objects
        context.refresh();

        var webStarter = context.getBean(WebStarter.class);
        webStarter.run(context);

        return context;
    }
}
