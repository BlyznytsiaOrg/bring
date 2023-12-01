package com.bobocode.bring.web.actuator.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.experimental.UtilityClass;
import org.slf4j.LoggerFactory;

/**
 * Utility class for changing the log level of a specific package.
 * It provides a method to adjust the logging level for a given package name.
 * This class is used by the ActuatorController to dynamically modify log levels at runtime.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@UtilityClass
public class LogLevelChangerUtils {

    /**
     * Changes the log level for the specified package.
     *
     * @param packageName The name of the package for which to change the log level.
     * @param newLevel    The new log level to set.
     */
    public static void changeLogLevel(String packageName, String newLevel) {
        Logger logger = (Logger) LoggerFactory.getLogger(packageName);
        logger.setLevel(Level.toLevel(newLevel));
    }
}
