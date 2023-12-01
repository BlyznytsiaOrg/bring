package com.bobocode.bring.core.predestroy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.core.context.impl.BringApplicationContext;
import com.bobocode.bring.core.exception.PreDestroyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import testdata.predestroy.positive.Messenger;

public class PredestroyTest {

  private static final String TEST_DATA_PACKAGE = "testdata.predestroy";

  @DisplayName("Should invoke method marked by @PreDestroy annotation on context.close()")
  @Test
  void shouldInvokeMethodMarkedByPreDestroyAnnotation() {
    // given
    BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".positive");

    // when
    var messenger = bringApplicationContext.getBean(Messenger.class);
    bringApplicationContext.close();

    // then

    assertThat(messenger.getMessage()).isEqualTo("PreDestroy works fine");
  }

  @DisplayName("Should throw PreDestroyException on context.close()")
  @Test
  void shouldThrowPreDestroyException() {
    //given
    var expectedMessage = "@PreDestroy should be added to method without parameters";
    // when
    Executable executable = () -> {
      BringApplicationContext bringApplicationContext = BringApplication.run(TEST_DATA_PACKAGE + ".negative");
      bringApplicationContext.close();
    };

    // then
    PreDestroyException preDestroyException = assertThrows(PreDestroyException.class, executable);
    assertThat(preDestroyException.getMessage()).isEqualTo(expectedMessage);
  }

}
