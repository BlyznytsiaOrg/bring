package testdata.di.positive.qualifier.field;

import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Qualifier;
import io.github.blyznytsiaorg.bring.core.annotation.Service;
import lombok.Getter;

@Service
@Getter
public class PrintService {

  @Autowired
  @Qualifier("canonPrinter")
  private Printer printer;

}
