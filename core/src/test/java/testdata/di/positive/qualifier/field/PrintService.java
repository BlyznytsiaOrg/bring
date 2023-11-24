package testdata.di.positive.qualifier.field;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Qualifier;
import com.bobocode.bring.core.anotation.Service;
import lombok.Getter;

@Service
@Getter
public class PrintService {

  @Autowired
  @Qualifier("CanonPrinter")
  private Printer printer;

}
