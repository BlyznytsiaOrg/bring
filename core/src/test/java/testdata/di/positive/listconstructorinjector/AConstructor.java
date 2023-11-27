package testdata.di.positive.listconstructorinjector;


import com.bobocode.bring.core.annotation.Autowired;
import com.bobocode.bring.core.annotation.Component;
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
