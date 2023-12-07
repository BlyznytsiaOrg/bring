package testdata.di.positive.setfieldinjector;

import io.github.blyznytsiaorg.bring.core.annotation.Autowired;;
import io.github.blyznytsiaorg.bring.core.annotation.Component;
import lombok.Getter;

import java.util.Set;

@Getter
@Component
public class AField {
    @Autowired
    private Set<IA> set;
    public void talking() {
        set.forEach(IA::talk);
    }
}
