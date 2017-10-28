package com.chain.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import com.chain.logging.ChainUtilsLoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * URl工具类
 * 
 * 需要Jackson支持
 * 
 * @author Chain
 * @version 1.0
 *
 */
public class URLUtils {

	private static Logger logger = ChainUtilsLoggerFactory.getLogger(URLUtils.class);

	/**
	 * 发起Url并获取到返回的json
	 *
	 * @param sUrl
	 *            url地址
	 * @param method
	 *            请求方法(默认GET)
	 * @return 请求的结果
	 * @throws IOException
	 *             异常
	 */
	public static Map<String, Object> getJsonMapFromUrl(String sUrl, String method) throws IOException {
		URL url = new URL(sUrl);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod(method);
		httpURLConnection.connect();
		InputStream inputStream = httpURLConnection.getInputStream();
		byte[] buf = new byte[8 * 1024];
		int len = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		baos.flush();
		String res = baos.toString();
		baos.close();
		httpURLConnection.disconnect();

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> resMap = objectMapper.readValue(res, HashMap.class);
		return resMap;
	}

	/**
	 * 发起Url并获取到返回的json, GET请求
	 *
	 * @param sUrl
	 *            url地址
	 * @return 请求的结果
	 * @throws IOException
	 *             异常
	 */
	public static Map<String, Object> getJsonMapFromUrl(String sUrl) throws IOException {
		return getJsonMapFromUrl(sUrl, "GET");
	}

}
