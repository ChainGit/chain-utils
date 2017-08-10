package com.chain.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;

import com.chain.exception.ChainUtilsRuntimeException;
import com.chain.logging.ChainUtilsLoggerFactory;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

/**
 * 文件上传下载工具类<br>
 * 
 * 只能实现小文件简单的上传和下载<br>
 * 
 * @author Collected
 * @version 1.0
 *
 */
public class FileUpDownloadUtils {

	private static final Logger logger = ChainUtilsLoggerFactory.getLogger(FileUpDownloadUtils.class);

	/**
	 * POST方式发送表单<br>
	 * 
	 * HttpUrlConnection模拟HTTP消息<br>
	 * 
	 * 摘自：http://blog.csdn.net/wangpeng047/article/details/38303865<br>
	 * 
	 * @param urlStr
	 *            上传的目的url
	 * @param textMap
	 *            上传的文本域
	 * @param fileMap
	 *            上传的文件域
	 * @return 上传后的返回结果
	 * @throws Exception
	 *             异常
	 */
	public static String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap)
			throws Exception {
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------" + RandomStringUtils.generateString(15); // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator<Map.Entry<String, String>> iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}

			// file
			if (fileMap != null) {
				Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					MagicMatch match = Magic.getMagicMatch(file, false, true);
					String contentType = match.getMimeType();

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

					out.write(strBuf.toString().getBytes());

					DataInputStream in = new DataInputStream(new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			logger.error("form upload exception", e);
			throw e;
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}

	/**
	 * GET方式下载文件<br>
	 * 
	 * 注意下载的文件没有经过校验，可能下载的文件不正确，请使用FileVerifyUtils进行校验<br>
	 * 
	 * 只能下载小文件<br>
	 * 
	 * @param urlStr
	 *            URL
	 * @param file
	 *            文件名或路径
	 * @throws Exception
	 *             异常
	 */
	public static void download(String urlStr, String file) throws Exception {
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		BufferedOutputStream bos = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			conn.connect();
			InputStream is = conn.getInputStream();
			bis = new BufferedInputStream(is);
			File f = new File(file);
			if (f.exists())
				f.delete();
			baos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024 * 8];
			int len = -1;
			while ((len = bis.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			baos.flush();
			bis.close();
			byte[] data = baos.toByteArray();
			baos.close();

			bos = new BufferedOutputStream(new FileOutputStream(file));
			bos.write(data);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			logger.error("download file exception", e);
			throw e;
		} finally {

			try {
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				logger.error("io exception", e);
				throw new ChainUtilsRuntimeException("io exception", e);
			}
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				logger.error("io exception", e);
				throw new ChainUtilsRuntimeException("io exception", e);
			}
			try {
				if (bis != null)
					bis.close();
			} catch (IOException e) {
				logger.error("io exception", e);
				throw new ChainUtilsRuntimeException("io exception", e);
			}
		}
	}

}
