package testdata.di.positive.prototype.on;

import io.github.blyznytsiaorg.bring.core.annotation.Scope;
import io.github.blyznytsiaorg.bring.core.annotation.Service;
import io.github.blyznytsiaorg.bring.core.domain.BeanScope;
import io.github.blyznytsiaorg.bring.core.domain.ProxyMode;
import lombok.Getter;

import java.util.UUID;

@Getter
@Service
@Scope(name = BeanScope.PROTOTYPE, proxyMode = ProxyMode.ON)
public class Barista {
    
    private final UUID uuid = UUID.randomUUID();
    
}
