package testdata.predestroy.negative;

import io.github.blyznytsiaorg.bring.core.annotation.Component;
import io.github.blyznytsiaorg.bring.core.annotation.PreDestroy;
import lombok.Getter;

@Component
@Getter
public class Messenger {

  private String message;

  @PreDestroy
  public void onDestroy(String msg){
    message = msg;
  }

}
