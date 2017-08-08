package com.chain.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chain.exception.ChainUtilsRuntimeException;

/**
 * IPv4地址工具类：部分方法依赖servlet-api.jar
 * 
 * @author Chain Qian
 * @version 1.1
 *
 */

public class Inet4AddressUtils {

	private static final Logger logger = LoggerFactory.getLogger(Inet4AddressUtils.class);

	private static final int DEFAULT = -1;

	private static final String DOT = ".";

	private static final String REGX_DOT = "\\.";

	/**
	 * 
	 * 获取客户端真实的IPv4地址，但可能还是获取不到
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return IPv4地址
	 */
	public static String getRealInet4Address(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡得到本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					logger.error("unkown host exception", e);
					throw new ChainUtilsRuntimeException("unkown host exception", e);
				}
				ip = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ip != null && ip.length() > 15) {
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}
		return ip;
	}

	/**
	 * 是否是合法的IPv4地址
	 * 
	 * @param ip
	 *            IP字符串
	 * @return 是否合法
	 */
	public static boolean isInet4Address(String ip) {
		String[] ips = ip.split(REGX_DOT);
		if (ips.length != 4)
			return false;
		for (int i = 0; i < ips.length; i++) {
			int t = new Integer(ips[i]);
			if (t < 0 || t > 255)
				return false;
		}
		return true;
	}

	/**
	 * 将IPv4地址转为二进制字符串
	 * 
	 * @param ip
	 *            IP字符串
	 * @return 二进制形式
	 */
	public static String toBinaryString(String ip) {
		if (!isInet4Address(ip))
			return ip;
		long iip = parseString(ip);
		return ScaleConvertUtils.toBinaryString(iip);
	}

	/**
	 * 将IPv4地址转为十六进制字符串
	 * 
	 * @param ip
	 *            IP字符串
	 * @return 十六进制形式
	 */
	public static String toHexString(String ip) {
		if (!isInet4Address(ip))
			return ip;
		long iip = parseString(ip);
		return ScaleConvertUtils.toHexString(iip);
	}

	/**
	 * 将IPv4地址转为八进制字符串
	 * 
	 * @param ip
	 *            IP字符串
	 * @return 八进制形式
	 */
	public static String toOctalString(String ip) {
		if (!isInet4Address(ip))
			return ip;
		long iip = parseString(ip);
		return ScaleConvertUtils.toOctalString(iip);
	}

	/**
	 * 格式化为完整的IPv4地址
	 * 
	 * 比如192.168.10.1格式化成192.168.010.001
	 * 
	 * @param ip
	 *            IP字符串
	 * @return 完整形式
	 */
	private static String formatInet4Address(String ip) {
		if (!isInet4Address(ip))
			return ip;
		String[] ips = ip.split(REGX_DOT);
		for (int i = 0; i < ips.length; i++) {
			while (ips[i].length() != 3)
				ips[i] = "0" + ips[i];
		}
		return ips[0] + DOT + ips[1] + DOT + ips[2] + DOT + ips[3];
	}

	/**
	 * 将字符串IPv4地址转为long
	 * 
	 * @param ip
	 *            IP字符串
	 * @return 长整型long
	 */
	public static long parseString(String ip) {
		if (!isInet4Address(ip))
			return DEFAULT;
		String[] ips = ip.split(REGX_DOT);
		long i1 = new Long(ips[0]);
		long i2 = new Long(ips[1]);
		long i3 = new Long(ips[2]);
		long i4 = new Long(ips[3]);
		long res = 0;
		res = (i1 << 24) + (i2 << 16) + (i3 << 8) + i4;
		return res;
	}

	/**
	 * 将整形表示的IPv4地址转为IPv4字符串
	 * 
	 * @param ip
	 *            long表示的IP地址
	 * @return 点形式IP地址字符串
	 */
	public static String toInet4Address(long ip) {
		String hStr = ScaleConvertUtils.toHexString(ip);
		char[] chs = hStr.toCharArray();
		int len = chs.length;
		String s1 = (chs[len - 2] + "") + (chs[len - 1] + "");
		String s2 = (chs[len - 4] + "") + (chs[len - 3] + "");
		String s3 = (chs[len - 6] + "") + (chs[len - 5] + "");
		String s4 = (len - 8 == 0 ? chs[len - 8] + "" : "") + (chs[len - 7] + "");
		try {
			int i1 = ScaleConvertUtils.parseHexString(s1);
			int i2 = ScaleConvertUtils.parseHexString(s2);
			int i3 = ScaleConvertUtils.parseHexString(s3);
			int i4 = ScaleConvertUtils.parseHexString(s4);
			return i4 + DOT + i3 + DOT + i2 + DOT + i1;
		} catch (Exception e) {
			logger.error("ip convert error", e);
			throw new ChainUtilsRuntimeException("ip convert error", e);
		}
	}

	/**
	 * 将整形表示的IPv4地址转为IPv4字符串(格式化的)
	 * 
	 * @param ip
	 *            long表示的IP地址
	 * @return 格式化的点形式IP地址字符串
	 */
	public static String toFormatInet4Address(long ip) {
		String sip = toInet4Address(ip);
		sip = formatInet4Address(sip);
		return sip;
	}

	/**
	 * 将IPv4地址转为格式化的二进制字符串
	 * 
	 * @param ip
	 *            IP地址
	 * @return 格式化的二进制形式
	 */
	public static String toFormatBinaryString(String ip) {
		String s = toBinaryString(ip);
		StringBuffer sb = new StringBuffer(s);
		while (sb.length() < 32)
			sb.insert(0, 0);
		sb.insert(8, " ");
		sb.insert(16 + 1, " ");
		sb.insert(24 + 2, " ");
		return sb.toString();
	}

	/**
	 * 将IPv4地址转为格式化的十六进制字符串
	 * 
	 * @param ip
	 *            IP地址
	 * @return 格式化的十六进制字符串
	 */
	public static String toFormatHexString(String ip) {
		String s = toHexString(ip);
		StringBuffer sb = new StringBuffer(s);
		while (sb.length() < 8)
			sb.insert(0, 0);
		sb.insert(2, " ");
		sb.insert(4 + 1, " ");
		sb.insert(6 + 2, " ");
		return sb.toString();
	}

}
