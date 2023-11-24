package com.bobocode.bring.web;

import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.web.servlet.WebStarter;

public class BringWebApplication {
    private static final String BRING_CORE_PACKAGE = "com.bobocode.bring";
    private static final String BRING_WEB_PACKAGE = BRING_CORE_PACKAGE + ".web";

    private BringWebApplication() {
    }

    /**
     * Run the Bring application context based on the provided base package for component scanning.
     *
     * @param basePackage the base package to scan for annotated beans
     * @return the initialized {@link BringApplicationContext} instance
     * @see BringApplicationContext
     */
    public static BringApplicationContext run(String basePackage) {
        // Create context: register Bean definitions
        String[] bringPackages = new String[]{BRING_CORE_PACKAGE, BRING_WEB_PACKAGE, basePackage};
        BringApplicationContext context = new BringApplicationContext(bringPackages);

        // Invoke Bean Post Processors, create Bean objects
        context.refresh();

        // Start Web Server and add bringContext to it.
        var webStarter = context.getBean(WebStarter.class);
        webStarter.run(context);

        return context;
    }


    /**
     * Run the Bring application context based on the provided configuration class.
     *
     * @param clazz the class containing configuration information and annotated beans
     * @return the initialized {@link BringApplicationContext} instance
     * @see BringApplicationContext
     */
    public static BringApplicationContext run(Class<?> clazz) {
        // Create context: register Bean definitions
        String[] bringPackages = new String[]{BRING_CORE_PACKAGE, BRING_WEB_PACKAGE, clazz.getPackageName()};
        BringApplicationContext context = new BringApplicationContext(bringPackages);

        // Invoke Bean Post Processors, create Bean objects
        context.refresh();

        // Start Web Server and add bringContext to it.
        var webStarter = context.getBean(WebStarter.class);
        webStarter.run(context);

        return context;
    }

    /**
     * Run the Bring application context based on the provided base package for component scanning.
     *
     * @param basePackages the base packages to scan for annotated beans
     * @return the initialized {@link BringApplicationContext} instance
     */
    public static BringApplicationContext run(String... basePackages) {
        // Create context: register Bean definitions
        String[] bringPackages = basePackages(basePackages);

        BringApplicationContext context = new BringApplicationContext(bringPackages);

        // Invoke Bean Post Processors, create Bean objects
        context.refresh();

        return context;
    }

    private static String[] basePackages(String... basePackage) {
        String[] bringPackages = new String[basePackage.length + 1];
        bringPackages[0] = BRING_CORE_PACKAGE;
        bringPackages[1] = BRING_WEB_PACKAGE;
        System.arraycopy(basePackage, 0, bringPackages, 2, basePackage.length);
        return bringPackages;
    }
}
