package testdata.di.positive.prototype.off;

import java.util.UUID;

import io.github.blyznytsiaorg.bring.core.annotation.Scope;
import io.github.blyznytsiaorg.bring.core.annotation.Service;
import io.github.blyznytsiaorg.bring.core.domain.BeanScope;
import io.github.blyznytsiaorg.bring.core.domain.ProxyMode;
import lombok.Getter;

@Getter
@Service
@Scope(name = BeanScope.PROTOTYPE, proxyMode = ProxyMode.OFF)
public class Barista {
    
    private final UUID uuid = UUID.randomUUID();
    
}
