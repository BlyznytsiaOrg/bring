package testdata.predestroy;

import com.bobocode.bring.core.annotation.Component;
import com.bobocode.bring.core.annotation.PreDestroy;
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
