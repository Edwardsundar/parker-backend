package com.app.parker.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);
    public static void info(String message) {
        logger.info(message);
    }
    public static void logError(String message) {
        logger.error(message);
    }
    public static void logWarning(String message) {
        logger.warn(message);
    }
    public static void logDebug(String message) {
        logger.debug(message);
    }
    public static void logTrace(String message) {
        logger.trace(message);
    }
}
