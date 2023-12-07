package io.github.blyznytsiaorg.bring.web;

import io.github.blyznytsiaorg.bring.core.context.impl.BringApplicationContext;
import io.github.blyznytsiaorg.bring.core.utils.Banner;
import io.github.blyznytsiaorg.bring.web.servlet.WebStarter;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@code BringWebApplication} class provides static methods for running the Bring application context.
 * It offers several ways to initialize and configure the application context based on different parameters.
 * <p>
 * The Bring application context includes packages for core components, web components, and additional
 * packages specified during the initialization.
 * </p>
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Slf4j
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
        // print banner
        Banner.printBanner();

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


    /**
     * Run the Bring application context based on the provided configuration class.
     *
     * @param clazz the class containing configuration information and annotated beans
     * @return the initialized {@link BringApplicationContext} instance
     * @see BringApplicationContext
     */
    public static BringApplicationContext run(Class<?> clazz) {
        // print banner
        Banner.printBanner();

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

    /**
     * Run the Bring application context based on the provided base package for component scanning.
     *
     * @param basePackages the base packages to scan for annotated beans
     * @return the initialized {@link BringApplicationContext} instance
     */
    public static BringApplicationContext run(String... basePackages) {
        // print banner
        Banner.printBanner();

        // Create context: register Bean definitions
        String[] bringPackages = basePackages(basePackages);

        BringApplicationContext context = new BringApplicationContext(bringPackages);

        // Invoke Bean Post Processors, create Bean objects
        context.refresh();

        // Start Web Server and add bringContext to it.
        var webStarter = context.getBean(WebStarter.class);
        webStarter.run(context);

        return context;
    }

    private static String[] basePackages(String... basePackage) {
        String[] bringPackages = new String[basePackage.length + 2];
        bringPackages[0] = BRING_CORE_PACKAGE;
        bringPackages[1] = BRING_WEB_PACKAGE;
        System.arraycopy(basePackage, 0, bringPackages, 2, basePackage.length);
        return bringPackages;
    }
}
