package io.github.blyznytsiaorg.bring.core.banner;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blyznytsiaorg.bring.core.utils.Banner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

class BannerTest {

    public static final String ON = "ON";
    public static final String OFF = "OFF";
    private final Properties originalProperties = new Properties();

    @BeforeEach
    void setUp() {
        originalProperties.putAll(System.getProperties());
    }


    @Test
    @DisplayName("should print default banner")
    void shouldPrintDefaultBanner() {
        // given
        // Redirect System.out to capture the printed content
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // when
        // Test with default mode (ON) and file settings
        System.setProperty(Banner.BRING_BANNER_KEY, ON);
        Banner.printBanner();
        String actualResult = outputStreamCaptor.toString().trim();

        // then
        // Validate the output
        assertThat(actualResult).isEqualTo(Banner.DEFAULT_BANNER_CONTENT.trim());
    }

    @Test
    @DisplayName("should print custom banner from file")
    void shouldPrintCustomBannerFromFile() throws IOException {
        // given
        // Redirect System.out to capture the printed content
        String bannerContent = new String(Files.readAllBytes(Path.of("src/test/resources/banner.txt")));
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // when
        // Test with default mode (ON) and file settings
        System.setProperty(Banner.BRING_BANNER_KEY, ON);
        System.setProperty(Banner.BRING_BANNER_FILE_KEY, Banner.BRING_BANNER_FILE_VALUE);
        Banner.printBanner();

        // then
        // Validate that nothing is printed
        assertThat(outputStreamCaptor.toString().trim()).hasToString(bannerContent.trim());
    }

    @Test
    @DisplayName("should not print banner")
    void shouldNotPrintBanner() {
        // given
        // Redirect System.out to capture the printed content
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // when
        // Test with default mode (OFF)
        System.setProperty(Banner.BRING_BANNER_KEY, OFF);
        Banner.printBanner();

        // then
        // Validate that nothing is printed
        assertThat(outputStreamCaptor.toString()).isEmpty();
    }

    @AfterEach
    void tearDown() {
        // Reset System.out
        System.setOut(System.out);
        // Restore the system properties to their original state
        System.setProperties(originalProperties);
    }
}
