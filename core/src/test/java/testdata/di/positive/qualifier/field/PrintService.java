package testdata.di.positive.qualifier.field;

import com.bobocode.bring.core.annotation.Autowired;
import com.bobocode.bring.core.annotation.Qualifier;
import com.bobocode.bring.core.annotation.Service;
import lombok.Getter;

@Service
@Getter
public class PrintService {

  @Autowired
  @Qualifier("CanonPrinter")
  private Printer printer;

}
