package testdata.contextcreation.nobringservlet;

import com.bobocode.bring.web.servlet.annotation.RequestMapping;
import com.bobocode.bring.web.servlet.annotation.RestController;

@RestController
@RequestMapping(path = "test")
public class NoBringServletImpl {
}
