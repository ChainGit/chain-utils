package com.chain.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.slf4j.Logger;

import com.chain.exception.ChainUtilsRuntimeException;
import com.chain.logging.ChainUtilsLoggerFactory;

/**
 * Java的压缩/解压缩默认实现是基于ZLib的
 * 
 * @author Chain Qian
 * @version 1.0
 *
 */
public class ZLibUtils {

	private static Logger logger = ChainUtilsLoggerFactory.getLogger(ZLibUtils.class);

	private ZLibUtils() {
	}

	private static final int BUFFER_SIZE = 1024;

	/**
	 * 压缩
	 * 
	 * @param data
	 *            待压缩数据
	 * @return byte[] 压缩后的数据
	 */
	public static byte[] compress(byte[] data) {
		byte[] output = new byte[0];

		Deflater compresser = new Deflater();

		compresser.reset();
		compresser.setInput(data);
		compresser.finish();

		ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[BUFFER_SIZE];
			while (!compresser.finished()) {
				int len = compresser.deflate(buf);
				baos.write(buf, 0, len);
			}
			baos.flush();
			output = baos.toByteArray();
		} catch (Exception e) {
			output = data;
			logger.error("compress", e);
			throw new ChainUtilsRuntimeException("compress");
		} finally {
			if (baos != null)
				try {
					baos.close();
				} catch (IOException e) {
				}
		}
		compresser.end();
		return output;
	}

	/**
	 * 压缩，结果输出到自定义流中
	 * 
	 * @param data
	 *            待压缩数据
	 * 
	 * @param os
	 *            输出流
	 */
	public static void compress(byte[] data, OutputStream os) {
		if (os == null)
			throw new ChainUtilsRuntimeException("output stream is null");

		DeflaterOutputStream dos = new DeflaterOutputStream(os);

		try {
			dos.write(data, 0, data.length);
			dos.finish();
			dos.flush();
		} catch (Exception e) {
			logger.error("compress", e);
			throw new ChainUtilsRuntimeException("compress");
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 解压缩
	 * 
	 * @param data
	 *            待压缩的数据
	 * @return byte[] 解压缩后的数据
	 */
	public static byte[] decompress(byte[] data) {
		byte[] output = new byte[0];

		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);

		ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[BUFFER_SIZE];
			while (!decompresser.finished()) {
				int len = decompresser.inflate(buf);
				baos.write(buf, 0, len);
			}
			baos.flush();
			output = baos.toByteArray();
		} catch (Exception e) {
			output = data;
			logger.error("decompress", e);
			throw new ChainUtilsRuntimeException("decompress");
		} finally {
			if (baos != null)
				try {
					baos.close();
				} catch (IOException e) {
				}
		}
		decompresser.end();
		return output;
	}

	/**
	 * 解压缩，从输入流读入数据
	 * 
	 * @param is
	 *            输入流
	 * @return byte[] 解压缩后的数据
	 */
	public static byte[] decompress(InputStream is) {
		if (is == null)
			throw new ChainUtilsRuntimeException("input stream is null");

		byte[] output = new byte[0];
		InflaterInputStream iis = new InflaterInputStream(is);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);
		try {
			byte[] buf = new byte[BUFFER_SIZE];
			int len = -1;
			// 输入流会自动的解压缩
			while ((len = iis.read(buf, 0, len)) > 0) {
				baos.write(buf, 0, len);
			}
			baos.flush();
			output = baos.toByteArray();
		} catch (IOException e) {
			logger.error("decompress", e);
			throw new ChainUtilsRuntimeException("decompress");
		} finally {
			if (iis != null)
				try {
					iis.close();
				} catch (IOException e) {
				}

			if (baos != null)
				try {
					baos.close();
				} catch (IOException e) {
				}
		}
		return output;
	}
}
