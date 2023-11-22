package com.bobocode.bring.core.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.experimental.UtilityClass;
import org.slf4j.LoggerFactory;

@UtilityClass
public class LogLevelChangerUtils {

    public static void changeLogLevel(String loggerName, Level newLevel) {
        Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
        logger.setLevel(newLevel);
    }
}
