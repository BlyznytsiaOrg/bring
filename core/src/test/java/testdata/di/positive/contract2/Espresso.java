package testdata.di.positive.contract2;

import com.bobocode.bring.core.anotation.Component;

@Component
public class Espresso implements Drink {
    @Override
    public String make() {
        return "Brewing a strong espresso!";
    }
}
