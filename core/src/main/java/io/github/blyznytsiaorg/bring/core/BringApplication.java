package io.github.blyznytsiaorg.bring.core;

import io.github.blyznytsiaorg.bring.core.context.impl.BringApplicationContext;
import io.github.blyznytsiaorg.bring.core.utils.Banner;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@code BringApplication} class provides a simple entry point to initialize and run a BringApplicationContext.
 * It allows the user to create and configure a {@link BringApplicationContext} either by specifying a
 * base package for component scanning or by providing a class that contains configuration information.
 *
 * <p>The class includes two static methods, {@link #run(Class)} and {@link #run(String)}, which both
 * perform the following steps:
 * <ol>
 *     <li>Create a new {@link BringApplicationContext} instance.</li>
 *     <li>Register Bean definitions based on the provided class or base package.</li>
 *     <li>Create Bean objects.</li>
 *     <li>Invoke Bean Post Processors.</li>
 * </ol>
 *
 * <p>Usage examples:
 * <pre>{@code
 * // Example 1: Using a configuration class
 * BringApplicationContext context = BringApplication.run(MyConfigurationClass.class);
 *
 * // Example 2: Using a base package for component scanning
 * BringApplicationContext context = BringApplication.run("com.example.myapp");
 * }</pre>
 *
 * <p>The {@code BringApplication} class is designed with a private constructor to enforce the use of
 * its static methods for initializing and running the application context.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Slf4j
public class BringApplication {
    private static final String BRING_PACKAGE = "io.github.blyznytsiaorg";

    /**
     * Private constructor to prevent instantiation of the class.
     * Instances of this class should be created using the static methods {@link #run(Class)} or {@link #run(String)}.
     */
    private BringApplication() {
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
        String[] bringPackages = new String[]{BRING_PACKAGE, clazz.getPackageName()};
        BringApplicationContext context = new BringApplicationContext(bringPackages);
        log.info("Starting application");
        // Invoke Bean Post Processors, create Bean objects
        context.refresh();
        
        return context;
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
        String[] bringPackages = new String[]{BRING_PACKAGE, basePackage};
        BringApplicationContext context = new BringApplicationContext(bringPackages);

        // Invoke Bean Post Processors, create Bean objects
        context.refresh();

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

        return context;
    }

    private static String[] basePackages(String... basePackage) {
        String[] bringPackages = new String[basePackage.length + 1];
        bringPackages[0] = BRING_PACKAGE;
        System.arraycopy(basePackage, 0, bringPackages, 1, basePackage.length);
        return bringPackages;
    }
    
}
