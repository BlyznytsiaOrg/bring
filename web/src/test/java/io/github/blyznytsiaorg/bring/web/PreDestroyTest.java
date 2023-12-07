package io.github.blyznytsiaorg.bring.web;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blyznytsiaorg.bring.core.context.impl.BringApplicationContext;
import io.github.blyznytsiaorg.bring.web.servlet.WebStarter;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testdata.predestroy.Messenger;

public class PreDestroyTest {
    public static final String PACKAGE = "testdata.predestroy";

    @Test
    @DisplayName("should invoke method annotated with preDestroy on WebServer stop")
    void shouldInvokeMethodAnnotatedWithPreDestroyOnWebServerStop() throws URISyntaxException, IOException, InterruptedException {
        //given
        BringApplicationContext bringApplicationContext = BringWebApplication.run(PACKAGE);

        //when
        var webStarter = bringApplicationContext.getBean(WebStarter.class);
        webStarter.stop();
        var messenger = bringApplicationContext.getBean(Messenger.class);

        //then
        assertThat(messenger.getMessage()).isEqualTo("PreDestroy works fine");
    }
}
