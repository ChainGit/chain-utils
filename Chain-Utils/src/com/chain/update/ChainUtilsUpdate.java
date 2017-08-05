package com.chain.update;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import com.chain.utils.FileVerifyUtils;

/**
 * chain-utils更新类
 * 
 * @author Chain Qian
 * @version 1.1
 *
 */
public class ChainUtilsUpdate {

	// 只有version
	private static Properties localProp = null;

	// 包含version,CRC32,SHA1,MD5
	private static Properties serverProp = null;

	private static String version = null;

	private static final String baseURL = "http://www.leechain.top/uploads/chain-utils/latest/";

	private static final String baseName = "chain-utils-";

	private static final String tailName = ".jar";

	private static String fullName = null;

	private static String serverName = null;

	static {
		localProp = new Properties();
		InputStream is = ChainUtilsUpdate.class.getClassLoader().getResourceAsStream("version.txt");
		try {
			localProp.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		version = localProp.getProperty("version");
		fullName = baseName + version + tailName;
		System.out.println("this local jar's current version is: " + fullName);
	}

	/**
	 * 检测本工具包是否是最新
	 * 
	 * @throws IOException
	 *             异常
	 * 
	 * @return 是否最新
	 */
	public static boolean check() throws IOException {
		URL url = new URL(baseURL + "version.txt");
		URLConnection conn = url.openConnection();
		InputStream is = conn.getInputStream();
		serverProp = new Properties();
		serverProp.load(is);
		String version = serverProp.getProperty("version");
		is.close();
		serverName = baseName + version + tailName;
		if (fullName.equals(serverName)) {
			System.out.println("this local jar is latest.");
			return true;
		} else {
			System.out.println("this local jar is not the latest, the latest one is: " + serverName);
			return false;
		}
	}

	/**
	 * 下载最新版的工具包，放在baseDir下
	 * 
	 * @param baseDir
	 *            保存的路径
	 * @throws IOException
	 *             异常
	 */
	public static void update(String baseDir) throws IOException {
		if (check())
			return;
		URL url = new URL(baseURL + serverName);
		URLConnection conn = url.openConnection();
		InputStream is = conn.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(is);
		String filePath = baseDir + File.separator + serverName;
		File f = new File(filePath);
		if (f.exists())
			f.delete();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] buf = new byte[1024 * 8];
		while (bis.read(buf) != -1) {
			bos.write(buf);
		}
		bos.flush();
		bis.close();
		bos.close();

		File file = new File(filePath);
		String absolutePath = file.getAbsolutePath();
		if (verify(absolutePath, serverProp))
			System.out.println("the latest jar has been downloaded at " + absolutePath);
		else {
			file.deleteOnExit();
			System.out.println("the downloaded jar file " + absolutePath
					+ " is not correct and has been removed, please try again.");
		}
	}

	/**
	 * 下载最新版的工具包，放在当前目录
	 * 
	 * @throws IOException
	 *             异常
	 * 
	 */
	public static void update() throws IOException {
		update("");
	}

	/**
	 * 校验指定路径的工具包，并与version.txt内容进行比较
	 * 
	 * @throws IOException
	 *             异常
	 * @return 下载的是否完整
	 * 
	 */
	private static boolean verify(String filePath, Properties version) {
		boolean isOk = false;
		try {
			String vcrc32 = version.getProperty("CRC32");
			String vmd5 = version.getProperty("MD5");
			String vsha1 = version.getProperty("SHA1");
			String fcrc32 = FileVerifyUtils.verify(FileVerifyUtils.CRC32, filePath);
			String fmd5 = FileVerifyUtils.verify(FileVerifyUtils.MD5, filePath);
			String fsha1 = FileVerifyUtils.verify(FileVerifyUtils.SHA1, filePath);
			if (vcrc32.equals(fcrc32) && vmd5.equals(fmd5) && vsha1.equals(fsha1))
				isOk = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOk;
	}

}
