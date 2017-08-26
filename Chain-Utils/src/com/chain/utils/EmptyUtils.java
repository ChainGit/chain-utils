package com.chain.utils;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 是否为空的判断
 * 
 * @author Chain
 * @version 1.0
 */
public class EmptyUtils {

	private static Logger logger = LoggerFactory.getLogger(EmptyUtils.class);

	/**
	 * 判断字符串是否为空
	 * 
	 * @param s
	 *            字符串
	 * @return 是否为空
	 */
	public static boolean isEmpty(String s) {
		if (s == null || s.length() < 1)
			return true;
		return false;
	}

	/**
	 * 判断集合是否为空
	 * 
	 * @param coll
	 *            集合
	 * @return 是否为空
	 */
	public static Boolean isEmpty(Collection<?> coll) {
		return null == coll || coll.size() == 0;
	}

	/**
	 * 判断Map是否为空
	 * 
	 * @param map
	 *            Map
	 * @return 是否为空
	 */
	public static Boolean isEmpty(Map<?, ?> map) {
		return null == map || map.size() == 0;
	}

	/**
	 * 判断Object是否为空
	 * 
	 * @param obj
	 *            对象
	 * @return 是否为空
	 */
	public static Boolean isEmpty(Object obj) {
		return null == obj;
	}

	/**
	 * 判断Object[]是否为空
	 * 
	 * @param obj
	 *            对象
	 * @return 是否为空
	 */
	public static Boolean isEmpty(Object[] obj) {
		return null == obj || obj.length < 1;
	}

}
