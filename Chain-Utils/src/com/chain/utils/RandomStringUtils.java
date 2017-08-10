package com.chain.utils;

import java.util.Random;

import org.slf4j.Logger;

import com.chain.logging.ChainUtilsLoggerFactory;

/**
 * 生成随机字符串的工具类<br>
 * 
 * 范围: [0-9A-Za-z]
 * 
 * @author Chain Qian
 * @version 1.0
 * 
 *
 */
public class RandomStringUtils {

	private static final Logger logger = ChainUtilsLoggerFactory.getLogger(RandomStringUtils.class);

	public static final String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String LETTER_CHAR = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String NUMBER_CHAR = "0123456789";

	/**
	 * 返回一个定长的随机字符串(只包含大小写字母、数字)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(ALL_CHAR.charAt(random.nextInt(ALL_CHAR.length())));
		}
		return sb.toString();
	}

	/**
	 * 返回一个定长的随机<b>纯字母</b>字符串(只包含大小写字母)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateLetterString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(ALL_CHAR.charAt(random.nextInt(LETTER_CHAR.length())));
		}
		return sb.toString();
	}

	/**
	 * 返回一个定长的随机<b>纯<i>小写</i>字母</b>字符串(只包含小写字母)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateLowerString(int length) {
		return generateLetterString(length).toLowerCase();
	}

	/**
	 * 返回一个定长的随机<b>纯<i>大写</i>字母</b>字符串(只包含大写字母)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateUpperString(int length) {
		return generateLetterString(length).toUpperCase();
	}

	/**
	 * 生成一个定长的<b>纯0</b>字符串(只包含0)
	 * 
	 * @param length
	 *            字符串长度
	 * @return 纯0字符串
	 */
	public static String generateZeroString(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append('0');
		}
		return sb.toString();
	}

	/**
	 * 根据传入的param整型数组，对其洗牌后，然后再根据传入的length，截取数组的前length个，得到一个随机字符串。<br>
	 * 
	 * 比如param=[1,2,3,4,5]，洗牌后为param=[3,4,1,2,5]，假设length=3，那么返回的就是"341"。<br>
	 * 
	 * 1、注意可能有转换溢出问题，造成结果有误。<br>
	 * 2、length若超过param数组的长度则length=param.length。<br>
	 * 
	 * @param param
	 *            整型数组
	 * @param length
	 *            字符串长度
	 * @return 随机的字符串
	 */
	public static String generateFromIntArray(int[] param, int length) {
		Random rand = new Random();
		int len = param.length;
		if (length > len)
			length = len;
		for (int i = len; i > 1; i--) {
			int index = rand.nextInt(i);
			int tmp = param[index];
			param[index] = param[i - 1];
			param[i - 1] = tmp;
		}
		int result = 0;
		for (int i = 0; i < length; i++) {
			result = result * 10 + param[i];
		}
		return String.valueOf(result);
	}
}
