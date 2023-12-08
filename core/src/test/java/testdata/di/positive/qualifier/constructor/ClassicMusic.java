package testdata.di.positive.qualifier.constructor;

import io.github.blyznytsiaorg.bring.core.annotation.Component;

@Component
public class ClassicMusic implements Music{

  @Override
  public String getSong() {
    return "Beethoven - Moonlight Sonata";
  }
}
