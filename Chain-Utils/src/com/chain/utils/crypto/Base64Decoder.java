package com.chain.utils.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;

import com.chain.exception.ChainUtilsRuntimeException;
import com.chain.logging.ChainUtilsLoggerFactory;

/**
 * Base64解密
 * 
 * @author Collected
 * @version 1.0
 *
 */
public class Base64Decoder extends FilterInputStream {

	private static final Logger logger = ChainUtilsLoggerFactory.getLogger(Base64Decoder.class);

	private static final char[] chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '+', '/' };

	// A mapping between char values and six-bit integers
	private static final int[] ints = new int[128];

	static {
		for (int i = 0; i < 64; i++) {
			ints[chars[i]] = i;
		}
	}

	private int charCount;
	private int carryOver;

	public Base64Decoder(InputStream in) {
		super(in);
	}

	/**
	 * 获得实例
	 * 
	 * @param in
	 *            输入流
	 * @return 实例
	 */
	public Base64Decoder getInstance(InputStream in) {
		return new Base64Decoder(in);
	}

	public int read() throws IOException {
		// Read the next non-whitespace character
		int x = -1;
		do {
			try {
				x = in.read();
			} catch (IOException e) {
				logger.error("io exception", e);
				throw e;
			}
			if (x == -1) {
				return -1;
			}
		} while (Character.isWhitespace((char) x));
		charCount++;

		// The '=' sign is just padding
		if (x == '=') {
			return -1; // effective end of stream
		}

		// Convert from raw form to 6-bit form
		x = ints[x];

		// Calculate which character we're decoding now
		int mode = (charCount - 1) % 4;

		// First char save all six bits, go for another
		if (mode == 0) {
			carryOver = x & 63;
			return read();
		}
		// Second char use previous six bits and first two new bits,
		// save last four bits
		else if (mode == 1) {
			int decoded = ((carryOver << 2) + (x >> 4)) & 255;
			carryOver = x & 15;
			return decoded;
		}
		// Third char use previous four bits and first four new bits,
		// save last two bits
		else if (mode == 2) {
			int decoded = ((carryOver << 4) + (x >> 2)) & 255;
			carryOver = x & 3;
			return decoded;
		}
		// Fourth char use previous two bits and all six new bits
		else if (mode == 3) {
			int decoded = ((carryOver << 6) + x) & 255;
			return decoded;
		}
		return -1; // can't actually reach this line
	}

	public int read(byte[] buf, int off, int len) throws IOException {
		if (buf.length < (len + off - 1)) {
			logger.error("The input buffer is too small: " + len + " bytes requested starting at offset " + off
					+ " while the buffer " + " is only " + buf.length + " bytes long.");
			throw new ChainUtilsRuntimeException(
					"The input buffer is too small: " + len + " bytes requested starting at offset " + off
							+ " while the buffer " + " is only " + buf.length + " bytes long.");
		}

		// This could of course be optimized
		int i;
		for (i = 0; i < len; i++) {
			int x = -1;
			try {
				x = read();
			} catch (IOException e) {
				logger.error("io exception", e);
				throw new IOException("io exception", e);
			}
			if (x == -1 && i == 0) { // an immediate -1 returns -1
				return -1;
			} else if (x == -1) { // a later -1 returns the chars read so far
				break;
			}
			buf[off + i] = (byte) x;
		}
		return i;
	}

	/***
	 * 将Base64编码的字符串解码
	 *
	 * @param encoded
	 *            编码的字符串
	 * @return 解码后的字符串
	 */
	public static String decode(String encoded) {
		return new String(decodeToBytes(encoded));
	}

	/***
	 * 将Base64编码的字符串解码
	 *
	 * @param encoded
	 *            编码的字符串
	 * @return 解码后的byte数组
	 */
	public static byte[] decodeToBytes(String encoded) {
		byte[] bytes = null;
		try {
			bytes = encoded.getBytes("UTF-8");
		} catch (UnsupportedEncodingException ignored) {
			logger.error("unsupport encoding exception", ignored);
			throw new ChainUtilsRuntimeException("unsupport encoding exception", ignored);
		}

		Base64Decoder in = new Base64Decoder(new ByteArrayInputStream(bytes));

		ByteArrayOutputStream out = new ByteArrayOutputStream((int) (bytes.length * 0.67));

		try {
			byte[] buf = new byte[4 * 1024]; // 4K buffer
			int bytesRead;
			while ((bytesRead = in.read(buf)) != -1) {
				out.write(buf, 0, bytesRead);
			}
			return out.toByteArray();
		} catch (IOException ignored) {
			logger.error("io exception", ignored);
			throw new ChainUtilsRuntimeException("io exception", ignored);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					logger.error("io exception", e);
					throw new ChainUtilsRuntimeException("io exception", e);
				}

			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					logger.error("io exception", e);
					throw new ChainUtilsRuntimeException("io exception", e);
				}
		}
	}
}
