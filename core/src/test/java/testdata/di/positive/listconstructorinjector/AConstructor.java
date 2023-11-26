package testdata.di.positive.listconstructorinjector;


import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Component;
import lombok.Getter;

import java.util.List;

@Component
@Getter
public class AConstructor {

    private final List<IA> list;

    @Autowired
    public AConstructor(List<IA> list) {
        this.list = list;
    }
}
