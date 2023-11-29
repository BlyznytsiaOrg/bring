package testdata.di.positive.setfieldinjector;

import com.bobocode.bring.core.annotation.Autowired;;
import com.bobocode.bring.core.annotation.Component;
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
