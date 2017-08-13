package com.chain.logging;

import java.lang.reflect.Method;

import org.slf4j.Logger;

/**
 * 使得加入了slf4j实现log，且去除slf4j不影响功能
 * 
 * @author Chain
 *
 */
public class ChainUtilsLoggerFactory {

	private static final String SLF4J_LOGGERFACTORY = "org.slf4j.LoggerFactory";

	private static Class<?> slf4jClass = null;

	private static Method slf4jGetLogger = null;

	static {
		try {
			slf4jClass = Class.forName(SLF4J_LOGGERFACTORY);
			if (slf4jClass != null) {
				slf4jGetLogger = slf4jClass.getDeclaredMethod("getLogger", Class.class);
				slf4jGetLogger.setAccessible(true);
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 获得logger
	 * 
	 * @param clz
	 *            所在的类
	 * @return logger
	 */
	public static Logger getLogger(Class<?> clz) {
		Logger logger = null;
		if (slf4jClass != null) {
			try {
				logger = (Logger) slf4jGetLogger.invoke(slf4jClass, clz);
			} catch (Exception e) {

			}
		} else {
			logger = EmptyLogger.getInstance();
		}
		return logger;
	}

}
