package com.chain.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.slf4j.Logger;

import com.chain.exception.ChainUtilsRuntimeException;
import com.chain.logging.ChainUtilsLoggerFactory;

/**
 * 文件和文件夹工具类
 * 
 * @author Chain Qian
 * @version 1.0
 *
 */
public class FileDirectoryUtils {

	private static final Logger logger = ChainUtilsLoggerFactory.getLogger(FileDirectoryUtils.class);

	/**
	 * 移动文件（注意读写权限）<br>
	 * 
	 * 既是移动文件，也是可以重命名文件<br>
	 * 
	 * 例1：moveFile("D:/Dira/test.txt","E:/Tom/test.txt");<br>
	 * 例2：moveFile("D:/Dira/test.txt","E:/Tom/abc.txt");<br>
	 * 
	 * @param srcFilePath
	 *            源文件路径
	 * @param toFilePath
	 *            目的文件路径
	 * @throws IOException
	 *             异常
	 */
	public static void moveFile(File srcFilePath, File toFilePath) throws IOException {
		copyFile(srcFilePath, toFilePath);
		srcFilePath.delete();
	}

	public static void moveFile(String srcFilePath, String toFilePath) throws IOException {
		copyFile(new File(srcFilePath), new File(toFilePath));
		new File(srcFilePath).delete();
	}

	/**
	 * 复制文件（注意读写权限）<br>
	 * 
	 * 文件通道方式<br>
	 * 
	 * 例1：copyFile("D:/Dira/test.txt","E:/Tom/test.txt");<br>
	 * 例2：copyFile("D:/Dira/test.txt","E:/Tom/abc.txt");<br>
	 * 
	 * @param srcFilePath
	 *            源文件路径
	 * @param toFilePath
	 *            目的文件路径
	 * @throws IOException
	 *             异常
	 */
	public static void copyFile(File srcFilePath, File toFilePath) throws IOException {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(srcFilePath);
			fo = new FileOutputStream(toFilePath);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			logger.error("io exception", e);
			throw e;
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				logger.error("io exception", e);
				throw new ChainUtilsRuntimeException("io exception", e);
			}
		}
	}

	public static void copyFile(String srcFilePath, String toFilePath) throws IOException {
		copyFile(new File(srcFilePath), new File(toFilePath));
	}

	/**
	 * 移动文件夹<br>
	 * 
	 * 
	 * 例1：moveDirectory("D:/Dira","E:/Dira");<br>
	 * 例2：moveDirectory("D:/Dira","E:/Tom");<br>
	 * 
	 * @param from
	 *            原文件夹
	 * @param to
	 *            目的文件夹
	 * @throws IOException
	 *             异常
	 */
	public static void moveDirectory(String from, String to) throws IOException {
		copyDirectory(from, to);
		deleteDirectory(from);
	}

	/**
	 * 复制文件夹<br>
	 * 
	 * 例1：copyDirectory("D:/Dira","E:/Dira");<br>
	 * 例2：copyDirectory("D:/Dira","E:/Tom");<br>
	 * 
	 * @param from
	 *            原文件夹
	 * @param to
	 *            目的文件夹
	 * @throws IOException
	 *             异常
	 */
	public static void copyDirectory(String from, String to) throws IOException {
		File src = new File(from);
		File dest = new File(to);
		if (src.isDirectory()) {
			if (!dest.exists()) {
				dest.mkdir();
			}
			String files[] = src.list();
			for (String file : files) {
				// 递归复制
				copyDirectory(src + File.separator + file, dest + File.separator + file);
			}
		} else {
			copyFile(src, dest);
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 *            文件
	 */
	public static void deleteFile(String fileName) {
		File file = new File(fileName);
		file.delete();
	}

	/**
	 * 删除文件夹下的所有文件，包括文件夹本身<br>
	 * 
	 * @param file
	 *            文件夹
	 */
	public static void deleteDirectory(File file) {
		emptyDirectory(file);
		file.delete();
	}

	/**
	 * 删除文件夹下的所有文件，包括文件夹本身<br>
	 * 
	 * @param dir
	 *            文件夹
	 */
	public static void deleteDirectory(String dir) {
		File file = new File(dir);
		deleteDirectory(file);
		file.delete();
	}

	/**
	 * 清空文件夹下的所有文件，不包括文件夹本身<br>
	 * 
	 * @param file
	 *            文件夹
	 */
	public static void emptyDirectory(File file) {
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File f : files) {
					deleteDirectory(f);
				}
			} else {
				file.delete();
			}
		}
	}

}
