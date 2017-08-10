package com.chain.utils.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;

import com.chain.exception.ChainUtilsRuntimeException;
import com.chain.logging.ChainUtilsLoggerFactory;

/**
 * MD5加密，生成MD5字符串(32位)
 * 
 * @author Collected
 * @version 1.0
 *
 */
public class MD5Utils {

	private static final Logger logger = ChainUtilsLoggerFactory.getLogger(MD5Utils.class);

	private final static String[] strDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	private static String byteToArrayString(byte bByte) {
		int iRet = bByte;
		// System.out.println("iRet="+iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return strDigits[iD1] + strDigits[iD2];
	}

	@SuppressWarnings("unused")
	private String byteToNum(byte bByte) {
		int iRet = bByte;
		System.out.println("iRet1=" + iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		return String.valueOf(iRet);
	}

	private static String byteToString(byte[] bByte) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++) {
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}

	/**
	 * MD5加密
	 * 
	 * @param strObj
	 *            原字符串
	 * @return 加密字符串
	 */
	public static String encrypt(String strObj) {
		String resultString = null;
		try {
			resultString = new String(strObj);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteToString(md.digest(strObj.getBytes()));
		} catch (NoSuchAlgorithmException ex) {
			logger.error("no such algorithm exception", ex);
			throw new ChainUtilsRuntimeException("no such algorithm exception", ex);
		}
		return resultString;
	}

	/**
	 * 返回新的实例
	 * 
	 * @return 实例
	 */
	public static MD5Utils getInstance() {
		return new MD5Utils();
	}
}
