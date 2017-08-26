package com.chain.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Number类的工具类
 * 
 * @author Chain
 * @version 1.0
 */
public class NumberUtils {

	private static Logger logger = LoggerFactory.getLogger(NumberUtils.class);

	/**
	 * 是否为0
	 * 
	 * @param number
	 *            要判断的数字
	 * @return 结果
	 */
	public static Boolean isZero(Number number) {
		if (number instanceof Integer || number instanceof Short || number instanceof Byte || number instanceof Long
				|| number instanceof BigInteger) {
			Long t = number.longValue();
			if (t == 0)
				return true;
		} else if (number instanceof Float || number instanceof Double || number instanceof BigDecimal) {
			BigDecimal t = new BigDecimal(number.doubleValue());
			if (t.equals(new BigDecimal(0)))
				return true;
		}
		return false;
	}

	/**
	 * 是否为正数
	 * 
	 * @param number
	 *            要判断的数字
	 * @return 结果
	 */
	public static Boolean isPositive(Number number) {
		if (number instanceof Integer || number instanceof Short || number instanceof Byte || number instanceof Long
				|| number instanceof BigInteger) {
			Long t = number.longValue();
			if (t > 0)
				return true;
		} else if (number instanceof Float || number instanceof Double || number instanceof BigDecimal) {
			BigDecimal t = new BigDecimal(number.doubleValue());
			if (t.compareTo(new BigDecimal(0)) > 0)
				return true;
		}
		return false;
	}

	/**
	 * 是否为负数
	 * 
	 * @param number
	 *            要判断的数字
	 * @return 结果
	 */
	public static Boolean isNegative(Number number) {
		if (number instanceof Integer || number instanceof Short || number instanceof Byte || number instanceof Long
				|| number instanceof BigInteger) {
			Long t = number.longValue();
			if (t < 0)
				return true;
		} else if (number instanceof Float || number instanceof Double || number instanceof BigDecimal) {
			BigDecimal t = new BigDecimal(number.doubleValue());
			if (t.compareTo(new BigDecimal(0)) < 0)
				return true;
		}
		return false;
	}

}
