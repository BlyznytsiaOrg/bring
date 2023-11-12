package data.beans;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Service;
import data.service.BringService;

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
