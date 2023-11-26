package com.bobocode.bring.web;

import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.web.servlet.WebStarter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BringWebApplication {
    private static final String BRING_CORE_PACKAGE = "com.bobocode.bring";
    private static final String BRING_WEB_PACKAGE = BRING_CORE_PACKAGE + ".web";

    private BringWebApplication() {
    }

    public static BringApplicationContext run(String basePackage) {
        // Create context: register Bean definitions
        String[] bringPackages = new String[]{BRING_CORE_PACKAGE, BRING_WEB_PACKAGE, basePackage};
        log.info("Starting {} using Java {}", basePackage, System.getProperty("java.version"));
        BringApplicationContext context = new BringApplicationContext(bringPackages);

        // Invoke Bean Post Processors, create Bean objects
        context.refresh();

        // Start Web Server and add bringContext to it.
        var webStarter = context.getBean(WebStarter.class);
        webStarter.run(context);

        return context;
    }

    public static BringApplicationContext run(Class<?> clazz) {
        // Create context: register Bean definitions
        String[] bringPackages = new String[]{BRING_CORE_PACKAGE, BRING_WEB_PACKAGE, clazz.getPackageName()};
        log.info("Starting {} using Java {}", clazz, System.getProperty("java.version"));
        BringApplicationContext context = new BringApplicationContext(bringPackages);

        // Invoke Bean Post Processors, create Bean objects
        context.refresh();

        // Start Web Server and add bringContext to it.
        var webStarter = context.getBean(WebStarter.class);
        webStarter.run(context);

        return context;
    }
}
