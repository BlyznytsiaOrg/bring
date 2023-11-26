package testdata.di.negative.noclassimplementation;

import lombok.ToString;

@ToString
//@Component
public class Latte {
    public String make() {
        return "Making a delicious latte!";
    }
}
