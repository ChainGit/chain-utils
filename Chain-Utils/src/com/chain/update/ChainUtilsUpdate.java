package com.chain.update;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

/**
 * chain-utils更新类
 * 
 * @author Chain Qian
 * @version 1.2.1
 *
 */
public class ChainUtilsUpdate {

	// 只有version
	private static Properties localProp = null;

	// 包含version,CRC32,SHA1,MD5
	private static Properties serverProp = null;

	private static String version = null;

	private static final String baseURL = "https://github.com/ChainGit/chain-utils/tree/master/Chain-Utils/web/";

	private static final String latestVersionTxt = "https://raw.githubusercontent.com/ChainGit/chain-utils/master/Chain-Utils/web/latest/version.txt";

	private static final String latestVersionJar = "https://github.com/ChainGit/chain-utils/raw/master/Chain-Utils/web/latest/chain-utils-latest.jar";

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
	 * 将生成的jar进行部署(仅限本工具类的开发者使用)
	 * 
	 * @param dir
	 *            基础路径
	 * @throws Exception
	 *             异常
	 */
	public static void deploy(String dir) throws Exception {
		File baseDir = new File(dir);
		if (!baseDir.exists())
			baseDir.mkdirs();
		File file = new File("");
		String workPath = file.getAbsolutePath();
		// System.out.println(workPath);
		String latestVersionPath = dir + File.separator + "latest";
		ChainUtilsUpdateUtils fdu = new ChainUtilsUpdateUtils();
		File latestVersionDir = new File(latestVersionPath);
		if (latestVersionDir.exists())
			fdu.emptyDirectory(latestVersionDir);
		latestVersionDir.mkdirs();
		File newestJar = new File(workPath + File.separator + "out" + File.separator + fullName);
		File javadocJar = new File(
				workPath + File.separator + "out" + File.separator + baseName + "javadoc" + tailName);
		ChainUtilsUpdateUtils.getInstance().copyFile(javadocJar,
				new File(latestVersionPath + File.separator + baseName + "javadoc" + tailName));
		String latestJar = baseName + "latest" + tailName;
		ChainUtilsUpdateUtils.getInstance().copyFile(newestJar,
				new File(latestVersionPath + File.separator + latestJar));
		String historyTxt = workPath + File.separator + "src" + File.separator + "history.txt";
		BufferedWriter bw = new BufferedWriter(new FileWriter(latestVersionPath + File.separator + "history.txt"));
		BufferedReader br = new BufferedReader(new FileReader(historyTxt));
		String buf = null;
		while ((buf = br.readLine()) != null) {
			bw.write(buf + "<br/>");
			bw.newLine();
		}
		bw.flush();
		br.close();
		bw.close();
		// FileDirectoryUtils.getInstance().copyFile(historyTxt, latestVersionPath +
		// File.separator + "history.txt");
		String versionTxt = workPath + File.separator + "src" + File.separator + "version.txt";
		ChainUtilsUpdateUtils.getInstance().copyFile(versionTxt, latestVersionPath + File.separator + "version.txt");
		String md5 = ChainUtilsUpdateUtils.verify(ChainUtilsUpdateUtils.MD5, newestJar.getAbsolutePath());
		String sha1 = ChainUtilsUpdateUtils.verify(ChainUtilsUpdateUtils.SHA1, newestJar.getAbsolutePath());
		String crc32 = ChainUtilsUpdateUtils.verify(ChainUtilsUpdateUtils.CRC32, newestJar.getAbsolutePath());
		BufferedOutputStream fos = new BufferedOutputStream(
				new FileOutputStream(new File(latestVersionPath + File.separator + "version.txt"), true));
		fos.write("\r\n".getBytes());
		fos.write(("MD5=" + md5 + "\r\n").getBytes());
		fos.write(("SHA1=" + sha1 + "\r\n").getBytes());
		fos.write(("CRC32=" + crc32 + "\r\n").getBytes());
		fos.flush();
		fos.close();
		ChainUtilsUpdateUtils.getInstance().copyFile(versionTxt,
				latestVersionPath + File.separator + "version_web.txt");
		BufferedOutputStream fos2 = new BufferedOutputStream(
				new FileOutputStream(new File(latestVersionPath + File.separator + "version_web.txt"), true));
		fos2.write("<br>".getBytes());
		fos2.write(("MD5=" + md5 + "<br>").getBytes());
		fos2.write(("SHA1=" + sha1 + "<br>").getBytes());
		fos2.write(("CRC32=" + crc32 + "<br>").getBytes());
		fos2.flush();
		fos2.close();
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
		URL url = new URL(latestVersionTxt);
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
		URL url = new URL(latestVersionJar);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		conn.connect();
		InputStream is = conn.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(is);
		String filePath = baseDir + File.separator + serverName;
		File f = new File(filePath);
		if (f.exists())
			f.delete();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024 * 8];
		int len = -1;
		while ((len = bis.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		baos.flush();
		bis.close();
		byte[] data = baos.toByteArray();
		baos.close();

		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		bos.write(data);
		bos.flush();
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
		update("." + File.separator);
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
			String fcrc32 = ChainUtilsUpdateUtils.verify(ChainUtilsUpdateUtils.CRC32, filePath);
			String fmd5 = ChainUtilsUpdateUtils.verify(ChainUtilsUpdateUtils.MD5, filePath);
			String fsha1 = ChainUtilsUpdateUtils.verify(ChainUtilsUpdateUtils.SHA1, filePath);
			if (vcrc32.equals(fcrc32) && vmd5.equals(fmd5) && vsha1.equals(fsha1))
				isOk = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOk;
	}

}
