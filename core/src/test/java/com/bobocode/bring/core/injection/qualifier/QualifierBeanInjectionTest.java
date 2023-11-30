package com.bobocode.bring.core.injection.qualifier;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.core.context.impl.BringApplicationContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testdata.di.positive.qualifier.constructor.MusicPlayer;
import testdata.di.positive.qualifier.field.PrintService;

import static org.assertj.core.api.Assertions.assertThat;

class QualifierBeanInjectionTest {

    private static final String TEST_DATA_PACKAGE = "testdata.di.positive";

    @DisplayName("Should autowire appropriate bean when we have 2 implementations and @Qualifier annotation")
    @Test
    void shouldRegisterQualifierComponent() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".qualifier.field");

        // when
        PrintService printer = bringApplicationContext.getBean(PrintService.class);

        // then
        assertThat(printer).isNotNull();
        assertThat(printer.getPrinter().print()).isEqualTo("Canon");
    }

    @DisplayName("Should find appropriate bean when we have 2 implementations and @Qualifier annotation")
    @Test
    void shouldInjectBeanViaConstructorByQualifier() {
        // given
        BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".qualifier.constructor");

        // when
        MusicPlayer musicPlayer = bringApplicationContext.getBean(MusicPlayer.class);

        // then
        assertThat(musicPlayer).isNotNull();
        assertThat(musicPlayer.playMusic()).isEqualTo("Beethoven - Moonlight Sonata Lynyrd Skynyrd - Sweet Home Alabama");
    }
}
