package com.bobocode.bring.core.env;

import java.util.Map;

public interface BringSourceLoader {

    String getFileExtensions();

    Map<String, String> load(String name);
}
