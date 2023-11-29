package testdata.di.positive.setconstructorinjector;


import com.bobocode.bring.core.annotation.Autowired;;
import com.bobocode.bring.core.annotation.Component;
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
