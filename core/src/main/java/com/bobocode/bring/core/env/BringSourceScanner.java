package com.bobocode.bring.core.env;

import java.io.File;
import java.util.List;

public interface BringSourceScanner {

    List<File> scan(String type);
}
