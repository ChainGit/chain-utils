package com.chain.utils;

import java.math.BigDecimal;

import org.slf4j.Logger;

import com.chain.exception.ChainUtilsRuntimeException;
import com.chain.logging.ChainUtilsLoggerFactory;

/**
 * 浮点数和双精度浮点数，与二进制字符串的相互转化
 * 
 * float遵从的是IEEE R32.24 而double遵从的是R64.53。
 * 
 * 无论是单精度还是双精度在存储中都分为三个部分：
 * 
 * 符号位(Sign) : 0代表正，1代表为负<br>
 * 指数位（Exponent）:用于存储科学计数法中的指数数据，并且采用移位存储<br>
 * 尾数部分（Mantissa）：尾数部分<br>
 * 
 * 注意：本工具的结果不一定准确
 * 
 * 和ScaleConvertUtils不同，本工具转换的方法是基于计算机存储浮点数原理的，而ScaleConvertUtil中关于浮点数的转换是基于JavaAPI实现的。
 * 
 * FloatDoubleConvertUtils的转换速度较慢。
 * 
 * C语言做法（更灵活）：http://blog.csdn.net/gjw198276/article/details/6956244
 * 
 * @author chain qian
 * @version 1.0
 *
 */
public class FloatDoubleConvertUtils {

	private static final Logger logger = ChainUtilsLoggerFactory.getLogger(FloatDoubleConvertUtils.class);

	private static BigDecimal bzero = new BigDecimal(0);

	private static final String STR_ZERO = "0";
	private static final String STR_ONE = "1";

	private static final int LEN_FLOAT = 32;
	private static final int LEN_FLOAT_SIGN = 1;
	private static final int LEN_FLOAT_EXPONENT = 8;
	private static final int LEN_FLOAT_MANTISSA = 23;

	private static final int LEN_DOUBLE = 64;
	private static final int LEN_DOUBLE_SIGN = 1;
	private static final int LEN_DOUBLE_EXPONENT = 11;
	private static final int LEN_DOUBLE_MANTISSA = 52;

	private static final int META_FLOAT = (1 << 7) - 1;
	private static final int META_DOUBLE = (1 << 10) - 1;

	/**
	 * 将float转为二进制字符串
	 * 
	 * @param f
	 *            单精度浮点数
	 * @return 二进制字符串
	 */
	public static String toBinaryString(float f) {
		BigDecimal bf = new BigDecimal(f);
		if (bf.equals(bzero))
			return STR_ZERO;

		String signStr = (bf.compareTo(bzero) < 0) ? STR_ONE : STR_ZERO;

		float absf = Math.abs(f);
		int intPart = (int) absf;

		if (intPart != 0) {
			String intPartString = ScaleConvertUtils.toBinaryString(intPart);
			int exponentLen = intPartString.length() - 1;
			int exponent = exponentLen + META_FLOAT;
			StringBuffer exponentSb = new StringBuffer(ScaleConvertUtils.toBinaryString(exponent));
			while (exponentSb.length() < LEN_FLOAT_EXPONENT)
				exponentSb.insert(0, STR_ZERO);
			int mantissaActualLen = LEN_FLOAT_MANTISSA - exponentLen;
			float floatPart = absf - intPart;
			StringBuffer mantissaSb = new StringBuffer(intPartString.substring(1));
			for (int i = 0; i < mantissaActualLen; i++) {
				float tf = floatPart * 2;
				int ti = (int) tf;
				mantissaSb.append(ti);
				floatPart = tf - ti;
			}
			return signStr + exponentSb.toString() + mantissaSb.toString();
		} else {
			StringBuffer mantissaSb = new StringBuffer();
			float floatPart = absf - intPart;
			for (int i = 0; i < LEN_FLOAT_MANTISSA; i++) {
				float tf = floatPart * 2;
				int ti = (int) tf;
				mantissaSb.append(ti);
				floatPart = tf - ti;
			}
			int firstOneIndex = mantissaSb.indexOf(STR_ONE) + 1;
			int exponent = -firstOneIndex + META_FLOAT;
			StringBuffer exponentSb = new StringBuffer(ScaleConvertUtils.toBinaryString(exponent));
			while (exponentSb.length() < LEN_FLOAT_EXPONENT)
				exponentSb.insert(0, STR_ZERO);
			mantissaSb = new StringBuffer(mantissaSb.substring(firstOneIndex));
			for (int i = mantissaSb.length(); i < LEN_FLOAT_MANTISSA; i++) {
				float tf = floatPart * 2;
				int ti = (int) tf;
				mantissaSb.append(ti);
				floatPart = tf - ti;
			}
			return signStr + exponentSb.toString() + mantissaSb.toString();
		}
	}

	/**
	 * 将二进制字符串转为float
	 * 
	 * @param s
	 *            二进制字符串
	 * @return 单精度浮点数
	 * @throws Exception
	 *             异常
	 */
	public static float parseBinaryStringToFloat(String s) throws Exception {
		if (s == null) {
			logger.error("string is null");
			throw new ChainUtilsRuntimeException("string is null");
		}

		if (s.equals(STR_ZERO))
			return 0.0f;

		if (s.length() != LEN_FLOAT) {
			logger.error("string length should be 32");
			throw new ChainUtilsRuntimeException("string length should be 32");
		}

		boolean sign = s.startsWith(STR_ONE);

		String exponent = s.substring(LEN_FLOAT_SIGN, LEN_FLOAT_SIGN + LEN_FLOAT_EXPONENT);
		String mantissa = s.substring(LEN_FLOAT_SIGN + LEN_FLOAT_EXPONENT, LEN_FLOAT);

		int exponentInt = ScaleConvertUtils.parseBinaryString(exponent) - META_FLOAT;
		StringBuffer intPartSb = new StringBuffer();
		StringBuffer floatPartSb = new StringBuffer();
		if (exponentInt < 0) {
			intPartSb.append(STR_ZERO);
			while (floatPartSb.length() + exponentInt + 1 < 0)
				floatPartSb.append(STR_ZERO);
			floatPartSb.append(STR_ONE);
			floatPartSb.append(mantissa);
		} else if (exponentInt == 0) {
			intPartSb.append(STR_ONE);
			floatPartSb.append(mantissa);
		} else {
			intPartSb.append(STR_ONE);
			intPartSb.append(mantissa.substring(0, exponentInt));
			floatPartSb.append(mantissa.substring(exponentInt));
		}

		// System.out.println(intPartSb + " " + floatPartSb);

		int intPart = ScaleConvertUtils.parseBinaryString(intPartSb.toString());

		char[] floatParts = floatPartSb.toString().toCharArray();
		float t = 1.0f;
		float floatPart = 0.0f;
		for (int i = 0; i < floatParts.length; i++) {
			t /= 2;
			floatPart += (floatParts[i] - 48) * t;
		}

		float res = intPart + floatPart;

		return sign ? -res : res;
	}

	/**
	 * 将double转为二进制字符串
	 * 
	 * @param d
	 *            双精度浮点数
	 * @return 二进制字符串
	 */
	public static String toBinaryString(double d) {
		BigDecimal bd = new BigDecimal(d);
		if (bd.equals(bzero))
			return STR_ZERO;

		String signStr = (bd.compareTo(bzero) < 0) ? STR_ONE : STR_ZERO;

		double absd = Math.abs(d);
		int intPart = (int) absd;

		if (intPart != 0) {
			String intPartString = ScaleConvertUtils.toBinaryString(intPart);
			int exponentLen = intPartString.length() - 1;
			int exponent = exponentLen + META_DOUBLE;
			StringBuffer exponentSb = new StringBuffer(ScaleConvertUtils.toBinaryString(exponent));
			while (exponentSb.length() < LEN_DOUBLE_EXPONENT)
				exponentSb.insert(0, STR_ZERO);
			int mantissaActualLen = LEN_DOUBLE_MANTISSA - exponentLen;
			double doublePart = absd - intPart;
			StringBuffer mantissaSb = new StringBuffer(intPartString.substring(1));
			for (int i = 0; i < mantissaActualLen; i++) {
				double td = doublePart * 2;
				int ti = (int) td;
				mantissaSb.append(ti);
				doublePart = td - ti;
			}
			return signStr + exponentSb.toString() + mantissaSb.toString();
		} else {
			StringBuffer mantissaSb = new StringBuffer();
			double doublePart = absd - intPart;
			for (int i = 0; i < LEN_DOUBLE_MANTISSA; i++) {
				double td = doublePart * 2;
				int ti = (int) td;
				mantissaSb.append(ti);
				doublePart = td - ti;
			}
			int firstOneIndex = mantissaSb.indexOf(STR_ONE) + 1;
			int exponent = -firstOneIndex + META_DOUBLE;
			StringBuffer exponentSb = new StringBuffer(ScaleConvertUtils.toBinaryString(exponent));
			while (exponentSb.length() < LEN_DOUBLE_EXPONENT)
				exponentSb.insert(0, STR_ZERO);
			mantissaSb = new StringBuffer(mantissaSb.substring(firstOneIndex));
			for (int i = mantissaSb.length(); i < LEN_DOUBLE_MANTISSA; i++) {
				double td = doublePart * 2;
				int ti = (int) td;
				mantissaSb.append(ti);
				doublePart = td - ti;
			}
			return signStr + exponentSb.toString() + mantissaSb.toString();
		}
	}

	/**
	 * 将二进制字符串转为double
	 * 
	 * @param s
	 *            二进制字符串
	 * @return 双精度浮点数
	 * @throws Exception
	 *             异常
	 */
	public static double parseBinaryStringToDouble(String s) throws Exception {
		if (s == null) {
			logger.error("string is null");
			throw new ChainUtilsRuntimeException("string is null");
		}

		if (s.equals(STR_ZERO))
			return 0.0f;

		if (s.length() != LEN_DOUBLE) {
			logger.error("string length should be 64");
			throw new ChainUtilsRuntimeException("string length should be 64");
		}

		boolean sign = s.startsWith(STR_ONE);

		String exponent = s.substring(LEN_DOUBLE_SIGN, LEN_DOUBLE_SIGN + LEN_DOUBLE_EXPONENT);
		String mantissa = s.substring(LEN_DOUBLE_SIGN + LEN_DOUBLE_EXPONENT, LEN_DOUBLE);

		int exponentInt = ScaleConvertUtils.parseBinaryString(exponent) - META_DOUBLE;
		StringBuffer intPartSb = new StringBuffer();
		StringBuffer doublePartSb = new StringBuffer();
		if (exponentInt < 0) {
			intPartSb.append(STR_ZERO);
			while (doublePartSb.length() + exponentInt + 1 < 0)
				doublePartSb.append(STR_ZERO);
			doublePartSb.append(STR_ONE);
			doublePartSb.append(mantissa);
		} else if (exponentInt == 0) {
			intPartSb.append(STR_ONE);
			doublePartSb.append(mantissa);
		} else {
			intPartSb.append(STR_ONE);
			intPartSb.append(mantissa.substring(0, exponentInt));
			doublePartSb.append(mantissa.substring(exponentInt));
		}

		// System.out.println(intPartSb + " " + doublePartSb);

		int intPart = ScaleConvertUtils.parseBinaryString(intPartSb.toString());

		char[] doubleParts = doublePartSb.toString().toCharArray();
		double t = 1.0f;
		double doublePart = 0.0f;
		for (int i = 0; i < doubleParts.length; i++) {
			t /= 2;
			doublePart += (doubleParts[i] - 48) * t;
		}

		double res = intPart + doublePart;

		return sign ? -res : res;
	}
}
