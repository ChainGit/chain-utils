package com.chain.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.chain.logging.ChainUtilsLoggerFactory;

/**
 * 
 * Cookie工具类
 * 
 * @author Chain Qian
 * @version 1.0
 *
 */
public class CookieUtils {

	private static final Logger logger = ChainUtilsLoggerFactory.getLogger(CookieUtils.class);

	/**
	 * 添加cookie
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param path
	 *            cookie路径
	 * @param name
	 *            cookie名字
	 * @param value
	 *            cookie值
	 * @param maxAge
	 *            cookie生命周期 以秒为单位
	 */
	public static void addCookie(HttpServletResponse response, String path, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		if (maxAge > 0)
			cookie.setMaxAge(maxAge);
		cookie.setPath(path);
		response.addCookie(cookie);
	}

	/**
	 * 删除cookie
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param name
	 *            cookie名称
	 * @param path
	 *            cookie路径
	 * @return 清空结果
	 */
	public static boolean removeCookie(HttpServletRequest request, HttpServletResponse response, String name,
			String path) {
		boolean bool = false;
		Cookie[] cookies = request.getCookies();
		if (null == cookies || cookies.length == 0)
			return bool;
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals(name)) {
				Cookie cookie = new Cookie(name, null);
				cookie.setMaxAge(0);
				cookie.setPath(path);// 根据你创建cookie的路径进行填写
				response.addCookie(cookie);
				bool = true;
			} else {
				response.addCookie(cookies[i]);
			}
		}
		return bool;
	}

	/**
	 * 删除所有的Cookie
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param path
	 *            cookie路径
	 * @param domain
	 *            cookie的域名
	 * @return 操作结果
	 */
	public static boolean clearCookie(HttpServletRequest request, HttpServletResponse response, String path,
			String domain) {
		boolean bool = false;
		Cookie[] cookies = request.getCookies();
		if (null == cookies || cookies.length == 0)
			return bool;
		for (int i = 0; i < cookies.length; i++) {
			String name = cookies[i].getName();
			Cookie cookie = new Cookie(name, null);
			cookie.setMaxAge(0);
			cookie.setPath(path);// 根据你创建cookie的路径进行填写
			cookie.setDomain(domain);
			response.addCookie(cookie);
		}
		bool = true;
		return bool;
	}

	/**
	 * 获取指定cookie
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param name
	 *            cookie名称
	 * @return cookie
	 */
	public static Cookie getCookieByName(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (null == cookies || cookies.length == 0)
			return null;
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			String cname = cookie.getName();
			if (cname.equals(name)) {
				return cookie;
			}
		}
		return null;
	}

}
