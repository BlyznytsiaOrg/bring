package testdata.contextcreation.nobringservlet;

import io.github.blyznytsiaorg.bring.web.servlet.annotation.RequestMapping;
import io.github.blyznytsiaorg.bring.web.servlet.annotation.RestController;

@RestController
@RequestMapping(path = "test")
public class NoBringServletImpl {
}
