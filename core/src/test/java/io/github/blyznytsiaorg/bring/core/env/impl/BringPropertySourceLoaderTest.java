package io.github.blyznytsiaorg.bring.core.env.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class BringPropertySourceLoaderTest {

    @DisplayName("Should load application properties")
    @Test
    void shouldLoadApplicationProperties() {
        //given
        var bringPropertySourceLoader = new BringPropertySourceLoader();
        String batterMode = "off";

        //when
        Map<String, String> properties = bringPropertySourceLoader.load("application-test.properties");

        //then
        assertThat(properties).isNotNull()
                        .hasSize(1)
                        .containsEntry("bring.main.banner-mode", batterMode);
    }

    @DisplayName("Should return empty map when properties file not found")
    @Test
    void shouldReturnEmptyMapWhenPropertiesFileNotExistWillUseDefaultOne() {
        //given
        var bringPropertySourceLoader = new BringPropertySourceLoader();
        String batterMode = "on";

        //when
        Map<String, String> properties = bringPropertySourceLoader.load("application-notFound.properties");

        //then
        assertThat(properties).isNotNull()
                .hasSize(1)
                .containsEntry("bring.main.banner-mode", batterMode);
    }

    @DisplayName("Should return empty map when properties has wrong type")
    @Test
    void shouldReturnDefaultApplicationWhenWePutWrongType() {
        //given
        var bringPropertySourceLoader = new BringPropertySourceLoader();
        String batterMode = "on";

        //when
        Map<String, String> properties = bringPropertySourceLoader.load("application.qqq");

        //then
        assertThat(properties).isNotNull()
                .hasSize(1)
                .containsEntry("bring.main.banner-mode", batterMode);
    }
}