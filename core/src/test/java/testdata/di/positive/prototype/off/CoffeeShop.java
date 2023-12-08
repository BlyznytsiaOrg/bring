package testdata.di.positive.prototype.off;

import java.util.UUID;

import io.github.blyznytsiaorg.bring.core.annotation.Scope;
import io.github.blyznytsiaorg.bring.core.annotation.Service;
import io.github.blyznytsiaorg.bring.core.domain.BeanScope;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Scope(name = BeanScope.SINGLETON)
@Service
public class CoffeeShop {
  
  private final UUID uuid = UUID.randomUUID();
  
  private final Barista barista;

}
