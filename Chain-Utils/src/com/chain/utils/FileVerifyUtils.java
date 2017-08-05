package com.chain.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * 文件校验 MD5,SHA1,CRC32
 *
 * @author Chain Qian
 * @version 1.0
 *
 */
public class FileVerifyUtils {

	public static final String MD5 = "MD5";
	public static final String SHA1 = "SHA1";
	public static final String CRC32 = "CRC32";

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	/**
	 * 校验文件，并获得校验结果(也是加密文件)
	 * 
	 * @param type
	 *            校验类型
	 * @param filePath
	 *            文件路径
	 * @return 校验字符串（加密字符串）
	 * @throws IOException
	 *             异常
	 */
	public static String verify(String type, String filePath) throws IOException {
		CheckedInputStream cis = null;
		File file = new File(filePath);
		if (CRC32.equals(type)) {
			cis = new CheckedInputStream(new FileInputStream(file), new CRC32());
			byte[] bytes = new byte[1024 * 8];
			while (cis.read(bytes) != -1)
				;
			long res = cis.getChecksum().getValue();
			cis.close();
			return Long.toHexString(res);
		} else {
			String value = null;
			FileInputStream is = null;
			try {
				is = new FileInputStream(filePath);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				MappedByteBuffer byteBuffer = is.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
				MessageDigest md5 = MessageDigest.getInstance(type);
				md5.update(byteBuffer);
				BigInteger bi = new BigInteger(1, md5.digest());
				value = bi.toString(16);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != is) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return value;
		}
	}

	/**
	 * Takes the raw bytes from the digest and formats them correct.
	 *
	 * @param bytes
	 *            the raw bytes from the digest.
	 * @return the formatted bytes.
	 */
	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		// 把密文转换成十六进制的字符串形式
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}

}