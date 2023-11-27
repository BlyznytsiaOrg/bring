package testdata.di.positive.prototype.off;

import java.util.UUID;

import com.bobocode.bring.core.annotation.Scope;
import com.bobocode.bring.core.annotation.Service;
import com.bobocode.bring.core.domain.BeanScope;
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
