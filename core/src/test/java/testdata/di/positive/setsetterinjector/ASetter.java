package testdata.di.positive.setsetterinjector;

import com.bobocode.bring.core.annotation.Autowired;;
import com.bobocode.bring.core.annotation.Component;
import lombok.Getter;

import java.util.Set;

@Getter
@Component
public class ASetter {

    private Set<IA> set;

    @Autowired
    public void setList(Set<IA> set) {
        this.set = set;
    }
}
