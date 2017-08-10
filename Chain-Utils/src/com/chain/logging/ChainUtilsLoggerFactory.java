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

	private static final String SLF4J_LOGGERFACTORY = "org.slf4j.LoggerFactory.class";

	private static Class<?> slf4jClass = null;
	static {
		try {
			slf4jClass = Class.forName(SLF4J_LOGGERFACTORY);
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
				Method m = slf4jClass.getDeclaredMethod("getLogger", Class.class);
				Object o = slf4jClass.newInstance();
				m.setAccessible(true);
				logger = (Logger) m.invoke(o, clz);
			} catch (Exception e) {

			}
		} else {
			logger = EmptyLogger.getInstance();
		}
		return logger;
	}

}
