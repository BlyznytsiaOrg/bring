package testdata.di.positive.mixconfigurationandservice;

import com.bobocode.bring.core.annotation.Component;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Component
public class BeanB {

    @Getter
    private final BeanA beanA;

}
