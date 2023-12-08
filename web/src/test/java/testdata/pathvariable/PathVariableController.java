package testdata.pathvariable;

import io.github.blyznytsiaorg.bring.web.servlet.BringServlet;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.DeleteMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.GetMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.PathVariable;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RestController;

@RestController
public class PathVariableController implements BringServlet {

    @GetMapping(path = "/str/{value}")
    public String getPathVariableString(@PathVariable String value) {
        return value;
    }

    @DeleteMapping(path = "/long/{value}")
    public String getPathVariableLong(@PathVariable Long value) {
        return value.getClass().getSimpleName();
    }

    @GetMapping(path = "/longAsString/{value}")
    public String getPathVariableLongAsString(@PathVariable Long value) {
        return value.toString();
    }

    @GetMapping(path = "/integer/{value}")
    public String getPathVariableInteger(@PathVariable Integer value) {
        return value.getClass().getSimpleName();
    }

    @GetMapping(path = "/double/{value}")
    public String getPathVariableDouble(@PathVariable Double value) {
        return value.getClass().getSimpleName();
    }

    @GetMapping(path = "/float/{value}")
    public String getPathVariableFloat(@PathVariable Float value) {
        return value.getClass().getSimpleName();
    }

    @GetMapping(path = "/short/{value}")
    public String getPathVariableFloat(@PathVariable Short value) {
        return value.getClass().getSimpleName();
    }

    @GetMapping(path = "/byte/{value}")
    public String getPathVariableByte(@PathVariable Byte value) {
        return value.getClass().getSimpleName();
    }
    @GetMapping(path = "/char/{value}")
    public Character getPathVariableCharacter(@PathVariable Character value) {
        return value;
    }

    @GetMapping(path = "/boolean/{value}")
    public boolean getPathVariableBoolean(@PathVariable boolean value) {
        return value;
    }

    @GetMapping(path = "/invalidType/{value}")
    public User getPathVariableInvalidType(@PathVariable User user) {
        return user;
    }

    public record User(String name, int age) {
    }
}
