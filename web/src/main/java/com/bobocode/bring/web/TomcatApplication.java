package com.bobocode.bring.web;

/**
 * The {@code TomcatApplication} class contains the main method to bootstrap the Bring application using Tomcat.
 * It serves as the entry point for starting the application context and initializing the embedded Tomcat server.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class TomcatApplication {

    /**
     * The main method to start the Bring application using Tomcat.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        BringWebApplication.run("com.bobocode.bring");
    }
}
