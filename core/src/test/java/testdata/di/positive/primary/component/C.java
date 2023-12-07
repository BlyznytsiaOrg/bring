package testdata.di.positive.primary.component;

import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Component;
import lombok.Getter;

@Component
@Getter
public class C {

  @Autowired
  private I field;

}
