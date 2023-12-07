package testdata.di.positive.qualifier.constructor;

import io.github.blyznytsiaorg.bring.core.annotation.Component;

@Component
public class RockMusic implements Music {

  @Override
  public String getSong() {
    return "Lynyrd Skynyrd - Sweet Home Alabama";
  }
}
