package testdata.di.positive.setconstructorinjector;


import io.github.blyznytsiaorg.bring.core.annotation.Autowired;;
import io.github.blyznytsiaorg.bring.core.annotation.Component;
import lombok.Getter;

import java.util.Set;

@Component
@Getter
public class AConstructor {

    private final Set<IA> set;

    @Autowired
    public AConstructor(Set<IA> set) {
        this.set = set;
    }
}
