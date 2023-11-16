package com.bobocode.bring.core.env.impl;

import com.bobocode.bring.core.env.BringSourceScanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BringPropertiesSourceScannerTest {

    @DisplayName("Should find two properties files")
    @Test
    void shouldFoundTwoPropertiesFiles() {
        //given
        BringSourceScanner bringSourceScanner = new BringPropertiesSourceScanner();

        //when
        List<File> files = bringSourceScanner.scan(".properties");

        //then
        assertThat(files).isNotNull()
                .hasSize(3);
    }

    @DisplayName("Should find zero yml files")
    @Test
    void shouldFoundZeroYmlFiles() {
        //given
        BringSourceScanner bringSourceScanner = new BringPropertiesSourceScanner();

        //when
        List<File> files = bringSourceScanner.scan(".yml");

        //then
        assertThat(files).isNotNull()
                .hasSize(0);
    }
}