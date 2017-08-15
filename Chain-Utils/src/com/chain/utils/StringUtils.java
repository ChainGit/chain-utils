package com.chain.utils;

/**
 * 字符串工具类
 * 
 * @author Chain Qian
 * @version 1.0
 *
 */
public class StringUtils {

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
}
