package com.chain.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件校验 MD5,SHA1,CRC32
 *
 * @author Chain Qian
 * @version 1.0
 *
 */
public class FileVerifyUtils {

	private static final Logger logger = LoggerFactory.getLogger(FileVerifyUtils.class);

	public static final String MD5 = "MD5";
	public static final String SHA1 = "SHA1";
	public static final String CRC32 = "CRC32";

	/**
	 * 校验文件，并获得校验结果(也是加密文件)
	 * 
	 * @param type
	 *            校验类型
	 * @param filePath
	 *            文件路径
	 * @return 校验字符串（加密字符串）
	 * @throws Exception
	 *             异常
	 */
	public static String verify(String type, String filePath) throws Exception {
		try {
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
				is = new FileInputStream(filePath);
				MappedByteBuffer byteBuffer = is.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
				MessageDigest md5 = MessageDigest.getInstance(type);
				md5.update(byteBuffer);
				BigInteger bi = new BigInteger(1, md5.digest());
				value = bi.toString(16);
				is.close();
				return value;
			}
		} catch (Exception e) {
			logger.error("verify file exception", e);
			throw e;
		}
	}

}