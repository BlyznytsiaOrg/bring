package testdata.di.positive.listfieldinjector;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Component;
import lombok.Getter;

import java.util.List;

@Getter
@Component
public class AField {
    @Autowired
    private List<IA> list;
    public void talking() {
        list.forEach(IA::talk);
    }
}