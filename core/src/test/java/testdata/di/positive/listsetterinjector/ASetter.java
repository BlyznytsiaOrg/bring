package testdata.di.positive.listsetterinjector;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Component;
import lombok.Getter;

import java.util.List;

@Getter
@Component
public class ASetter {

    private List<IA> list;

    @Autowired
    public void setList(List<IA> list) {
        this.list = list;
    }
}
