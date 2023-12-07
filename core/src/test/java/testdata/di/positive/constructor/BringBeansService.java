package testdata.di.positive.constructor;

import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Service;

@Service
public class BringBeansService {
    
    private final BringService bringService;
    
    private final BringComponent bringComponent;

    @Autowired
    public BringBeansService(BringService bringService, BringComponent bringComponent) {
        this.bringService = bringService;
        this.bringComponent = bringComponent;
    }
}
