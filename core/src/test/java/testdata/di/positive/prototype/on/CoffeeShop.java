package testdata.di.positive.prototype.on;

import com.bobocode.bring.core.anotation.Scope;
import com.bobocode.bring.core.anotation.Service;
import com.bobocode.bring.core.domain.BeanScope;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
@Scope(name = BeanScope.SINGLETON)
@Service
public class CoffeeShop {
  
  private final UUID uuid = UUID.randomUUID();
  
  private final Barista barista;

}
