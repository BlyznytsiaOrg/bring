package testdata.predestroy;

import io.github.blyznytsiaorg.bring.core.annotation.Component;
import io.github.blyznytsiaorg.bring.core.annotation.PreDestroy;
import lombok.Getter;

@Component
@Getter
public class Messenger {

  private String message;

  @PreDestroy
  public void onDestroy(){
    message = "PreDestroy works fine";
  }
}
