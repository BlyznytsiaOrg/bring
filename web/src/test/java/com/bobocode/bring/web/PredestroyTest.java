package com.bobocode.bring.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.web.servlet.WebStarter;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testdata.predestroy.Messenger;

public class PredestroyTest {

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
