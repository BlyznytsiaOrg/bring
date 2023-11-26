package testdata.contextcreation.nobringservlet;

import com.bobocode.bring.core.anotation.RestController;
import com.bobocode.bring.web.servlet.annotation.RequestMapping;

@RestController
@RequestMapping(path = "test")
public class NoBringServletImpl {
}
