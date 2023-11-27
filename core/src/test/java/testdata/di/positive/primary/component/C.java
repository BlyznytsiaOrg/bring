package testdata.di.positive.primary.component;

import com.bobocode.bring.core.annotation.Autowired;
import com.bobocode.bring.core.annotation.Component;
import lombok.Getter;

@Component
@Getter
public class C {

  @Autowired
  private I field;

}
