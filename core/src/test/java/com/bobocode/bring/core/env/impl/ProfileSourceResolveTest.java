package com.bobocode.bring.core.env.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileSourceResolveTest {

    private ProfileSourceResolve testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new ProfileSourceResolve();
    }

    @DisplayName("Should resolve application properties with profile")
    @Test
    void shouldResolveApplicationPropertiesWithProfile() {
        //when
        Map<String, String> properties = testInstance.resolve("test", ".properties");

        //then
        assertThat(properties).isNotNull()
                .hasSize(1)
                .containsEntry("bring.main.banner-mode", "off");
    }

    @DisplayName("Should resolve application properties with profile that not exist")
    @Test
    void shouldResolveApplicationPropertiesWithProfileThatNotExist() {
        //when
        Map<String, String> properties = testInstance.resolve("dev", ".properties");

        //then
        assertThat(properties).isNotNull()
                .hasSize(1)
                .containsEntry("bring.main.banner-mode", "on");
    }

    @DisplayName("Should resolve default application properties if profile not defined")
    @Test
    void shouldResolveDefaultApplicationPropertiesIfProfileNotDefined() {
        //when
        Map<String, String> properties = testInstance.resolve(null, ".properties");

        //then
        assertThat(properties).isNotNull()
                .hasSize(1)
                .containsEntry("bring.main.banner-mode", "on");
    }
}