package testdata.di.positive.listconstructorinjector;


import io.github.blyznytsiaorg.bring.core.annotation.Autowired;
import io.github.blyznytsiaorg.bring.core.annotation.Component;
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
