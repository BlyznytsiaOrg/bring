package com.bobocode.bring.core.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The {@code Banner} class provides functionality for printing a banner to the console,
 * either from a predefined string or custom banner by reading from a file.
 * <p>
 * Configuration options:
 * <ul>
 *     <li>{@link #BRING_BANNER_KEY}: Key for enabling/disabling the main banner. Default is "ON".</li>
 *     <li>{@link #BRING_BANNER_FILE_KEY}: Key for specifying the path to the banner file.
 *     Default is "resource/banner.txt".</li>
 * </ul>
 *
 * @see Mode
 */
@Slf4j
@UtilityClass
public class Banner {

    /**
     * Key for enabling/disabling the main banner. Default is "ON".
     */
    public static final String BRING_BANNER_KEY = "bring.main.banner";

    /**
     * Default value for enabling/disabling the main banner.
     */
    public static final String BRING_BANNER_VALUE = "OFF";

    /**
     * Key for specifying the path to the banner file. Default is "resources/banner.txt".
     */
    public static final String BRING_BANNER_FILE_KEY = "bring.main.banner.file";

    /**
     * Default value for specifying the path to the banner file.
     */
    public static final String BRING_BANNER_FILE_VALUE = "resources/banner.txt";

    /**
     * The banner.txt file.
     */
    public static final String BANNER_TXT = "banner.txt";

    /**
     * Message displayed when the resource folder is not found.
     */
    private static final String FILE_NOT_FOUND_MESSAGE = "banner.txt file can't be Null";


    /**
     * The default banner content.
     */
    public static final String DEFAULT_BANNER_CONTENT = """
                  ____       _              ______ \s
                 | __ ) _ __(_)_ __   __ _  \\ \\ \\ \\\s
                 |  _ \\| '__| | '_ \\ / _` |  | | | |
                 | |_) | |  | | | | | (_| |  | | | |
                 |____/|_|  |_|_| |_|\\__, |  | | | |
                 :: Bring :: (v1.0)  |___/  /_/_/_/\s
                                     
                """;

    /**
     * Prints the banner to the console based on the configured mode and file settings.
     */
    public static void printBanner() {
       Mode bannerMode = getBannerMode();
       if (bannerMode == Mode.ON) {
           if (hasToReadFromFile()) {
               try {
                   String bannerContent = new String(Files.readAllBytes(getPath()));
                   System.out.println(bannerContent);
               } catch (Exception e) {
                   log.warn("Can't print banner from the file {}", BRING_BANNER_FILE_VALUE);
               }
           } else {
               System.out.println(DEFAULT_BANNER_CONTENT);
           }
       }
   }

    /**
     * Gets the configured banner mode (ON or OFF).
     *
     * @return The configured banner mode.
     */
    private static Mode getBannerMode() {
        String propertyBannerMode = System.getProperty(BRING_BANNER_KEY);
        return BRING_BANNER_VALUE.equalsIgnoreCase(propertyBannerMode) ? Mode.OFF : Mode.ON;
    }

    /**
     * Checks if the banner should be read from a file based on the configuration.
     *
     * @return {@code true} if reading from a file is configured, {@code false} otherwise.
     */
    private static boolean hasToReadFromFile() {
        String bringFileToReadBanner = System.getProperty(BRING_BANNER_FILE_KEY);
        return BRING_BANNER_FILE_VALUE.equals(bringFileToReadBanner);
    }

    /**
     * Gets the path to the banner file, resolving it based on the classpath.
     *
     * @return The path to the banner file.
     */
    @SneakyThrows
    private static Path getPath() {
        URL fileUrl = Banner.class.getClassLoader().getResource(BANNER_TXT);
        Objects.requireNonNull(fileUrl, FILE_NOT_FOUND_MESSAGE);

        Path folderPath = Paths.get(fileUrl.toURI()).resolve("..").normalize();
        return Paths.get(folderPath.toString(), "/" + BANNER_TXT);
    }

    /**
     * An enumeration of possible values for configuring the Banner.
     */
    public enum Mode {

        /**
         * Disable printing of the banner.
         */
        OFF,

        /**
         * Print the banner to System.out.
         */
        ON
    }
}
