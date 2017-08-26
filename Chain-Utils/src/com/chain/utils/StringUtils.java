package com.chain.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字符串工具类
 * 
 * @author Chain Qian
 * @version 1.0
 *
 */
public class StringUtils {

	private static Logger logger = LoggerFactory.getLogger(StringUtils.class);

	/**
	 * 数组合并(增长)
	 *
	 * @param oldArry
	 *            原字符串数组
	 * @param newArry
	 *            新的字符串数组
	 * @return 衔接得字符串数组
	 */
	public static String[] concatStringArray(String[] oldArry, String[] newArry) {
		int lenOld = oldArry.length;
		int lenNew = newArry.length;
		String[] temp = new String[lenNew + lenOld];
		int i = 0;
		for (; i < lenOld; i++)
			temp[i] = oldArry[i];
		for (; i < lenNew + lenOld; i++)
			temp[i] = newArry[i - lenOld];
		return temp;
	}

}
