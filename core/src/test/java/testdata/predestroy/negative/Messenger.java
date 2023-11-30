package testdata.predestroy.negative;

import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.core.annotation.PreDestroy;
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
