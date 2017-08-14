package com.chain.utils.crypto;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;

import com.chain.exception.ChainUtilsRuntimeException;
import com.chain.logging.ChainUtilsLoggerFactory;

/**
 * 非对称加密算法RSA
 * 
 * @author Collected
 * @version 1.1
 * 
 * 
 */
public class RSAUtils {

	private static final Logger logger = ChainUtilsLoggerFactory.getLogger(RSAUtils.class);

	/** 指定加密算法为RSA */
	private static final String ALGORITHM = "RSA";
	/** 密钥长度，用来初始化 */
	private static final int KEYSIZE = 1024;
	/** 公钥 */
	private Key publicKey = null;
	/** 私钥 */
	private Key privateKey = null;
	/** 公钥字符串 */
	private String publicKeyString = null;
	/** 私钥字符串 */
	private String privateKeyString = null;

	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;
	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	public RSAUtils(Key publicKey, Key privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.publicKeyString = toKeyString(publicKey);
		this.privateKeyString = toKeyString(privateKey);
	}

	public RSAUtils(String publicKeyString, String privateKeyString) {
		this.publicKeyString = publicKeyString;
		this.privateKeyString = privateKeyString;
		this.privateKey = generatePublicKey(publicKeyString);
		this.publicKey = generatePrivateKey(privateKeyString);
	}

	public static RSAUtils getInstance(Key publicKey, Key privateKey) {
		return new RSAUtils(publicKey, privateKey);
	}

	public static RSAUtils getInstance(String publicKeyString, String privateKeyString) {
		return new RSAUtils(publicKeyString, privateKeyString);
	}

	/**
	 * 生成随机的公钥和私钥KEY密钥对，公钥KEY为第一个，私钥KEY为第二个
	 * 
	 * @return 密钥对
	 */
	public static Key[] generateRandomKeyPair() {
		// /** RSA算法要求有一个可信任的随机数源 */
		SecureRandom secureRandom = new SecureRandom();
		/** 为RSA算法创建一个KeyPairGenerator对象 */
		KeyPairGenerator keyPairGenerator = null;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			logger.error("no such algorithm exception", e);
			throw new ChainUtilsRuntimeException("no such algorithm exception", e);
		}
		/** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
		keyPairGenerator.initialize(KEYSIZE, secureRandom);
		/** 生成密匙对 */
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		/** 得到公钥 */
		Key publicKey = keyPair.getPublic();
		/** 得到私钥 */
		Key privateKey = keyPair.getPrivate();
		return new Key[] { publicKey, privateKey };
	}

	/**
	 * 生成随机的公钥和私钥KEY密钥对的字符串，公钥KEY为第一个，私钥KEY为第二个
	 * 
	 * @return 密钥对字符串
	 */
	public static String[] generateRandomKeyPairString() {
		Key[] keys = generateRandomKeyPair();
		return new String[] { toKeyString(keys[0]), toKeyString(keys[1]) };
	}

	/**
	 * 根据传入的公钥和私钥字符串生成KEY密钥对的字符串，公钥KEY为第一个，私钥KEY为第二个
	 * 
	 * @param publicKeyStr
	 *            公钥字符串
	 * @param privateKeyStr
	 *            私钥字符串
	 * @return 密钥对字符串
	 */
	public static String[] generateKeyPairString(String publicKeyStr, String privateKeyStr) {
		Key[] keys = generateKeyPair(publicKeyStr, privateKeyStr);
		return new String[] { toKeyString(keys[0]), toKeyString(keys[1]) };
	}

	/**
	 * 根据传入的公钥和私钥字符串生成KEY密钥对对象，公钥KEY为第一个，私钥KEY为第二个
	 * 
	 * @param publicKeyStr
	 *            公钥字符串
	 * @param privateKeyStr
	 *            私钥字符串
	 * @return 密钥对
	 */
	public static Key[] generateKeyPair(String publicKeyStr, String privateKeyStr) {
		Key[] keys = new Key[2];
		keys[0] = generatePublicKey(publicKeyStr);
		keys[1] = generatePrivateKey(privateKeyStr);
		return keys;
	}

	/**
	 * 从InputStream中加载公钥文件
	 * 
	 * @param in
	 *            公钥输入流
	 * @return Key对象
	 */
	public static Key loadPublicKey(InputStream in) {
		return generatePublicKey(readKey(in));

	}

	/**
	 * 从InputStream中加载私钥文件
	 * 
	 * @param in
	 *            输入流
	 * @return Key对象
	 */
	public static Key loadPrivateKey(InputStream in) {
		return generatePrivateKey(readKey(in));
	}

	/**
	 * 从InputStream中读取密钥字符串
	 * 
	 * @param in
	 *            输入流
	 * @return 密钥字符串
	 */
	private static String readKey(InputStream in) {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String readLine = null;
		StringBuilder sb = new StringBuilder();
		try {
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
		} catch (IOException e) {
			logger.error("io exception", e);
			throw new ChainUtilsRuntimeException("io exception", e);
		}

		return sb.toString();
	}

	/**
	 * 使用私钥KEY来解密
	 * 
	 * @param cryptoSrc
	 *            加密的字符串
	 * @return 解密的字符串
	 */
	public String decryptByPrivateKey(String cryptoSrc) {
		/** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			logger.error("no such exception", e);
			throw new ChainUtilsRuntimeException("no such exception", e);
		}
		// Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		try {
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
		} catch (InvalidKeyException e) {
			logger.error("invalid key exception", e);
			throw new ChainUtilsRuntimeException("invalid key exception", e);
		}
		byte[] encryptedData = Base64Decoder.decodeToBytes(cryptoSrc);

		/** 执行解密操作 */
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				try {
					cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
				} catch (IllegalBlockSizeException | BadPaddingException e) {
					logger.error("exception", e);
					throw new ChainUtilsRuntimeException("exception", e);
				}
			} else {
				try {
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				} catch (IllegalBlockSizeException | BadPaddingException e) {
					logger.error("exception", e);
					throw new ChainUtilsRuntimeException("exception", e);
				}
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		try {
			out.close();
		} catch (IOException e) {
			logger.error("io exception", e);
			throw new ChainUtilsRuntimeException("io exception", e);
		}
		return new String(decryptedData);
	}

	/**
	 * 使用公钥KEY来解密
	 * 
	 * @param cryptoSrc
	 *            解密的字符串
	 * @return 解密后的字符串
	 */
	public String decryptByPublicKey(String cryptoSrc) {
		/** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
		// Cipher cipher = Cipher.getInstance(ALGORITHM);
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			logger.error("no such exception", e);
			throw new ChainUtilsRuntimeException("no such exception", e);
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
		} catch (InvalidKeyException e) {
			logger.error("invalid key exception", e);
			throw new ChainUtilsRuntimeException("invalid key exception", e);
		}
		byte[] encryptedData = Base64Decoder.decodeToBytes(cryptoSrc);
		/** 执行解密操作 */

		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				try {
					cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
				} catch (IllegalBlockSizeException | BadPaddingException e) {
					logger.error("exception", e);
					throw new ChainUtilsRuntimeException("exception", e);
				}
			} else {
				try {
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				} catch (IllegalBlockSizeException | BadPaddingException e) {
					logger.error("exception", e);
					throw new ChainUtilsRuntimeException("exception", e);
				}
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		try {
			out.close();
		} catch (IOException e) {
			logger.error("io exception", e);
			throw new ChainUtilsRuntimeException("io exception", e);
		}

		return new String(decryptedData);
	}

	/**
	 * 使用私钥KEY来加密字符串
	 * 
	 * @param cryptoSrc
	 *            原字符串
	 * @return 加密的字符串
	 */
	public String encryptByPrivateKey(String cryptoSrc) {
		/** 得到Cipher对象来实现对源数据的RSA加密 */
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			logger.error("no such exception", e);
			throw new ChainUtilsRuntimeException("no such exception", e);
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		} catch (InvalidKeyException e) {
			logger.error("invalid key exception", e);
			throw new ChainUtilsRuntimeException("invalid key exception", e);
		}
		byte[] data = cryptoSrc.getBytes();
		/** 执行数据分组加密操作 */
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				try {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} catch (IllegalBlockSizeException | BadPaddingException e) {
					logger.error("exception", e);
					throw new ChainUtilsRuntimeException("exception", e);
				}
			} else {
				try {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				} catch (IllegalBlockSizeException | BadPaddingException e) {
					logger.error("exception", e);
					throw new ChainUtilsRuntimeException("exception", e);
				}
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		try {
			out.close();
		} catch (IOException e) {
			logger.error("io exception", e);
			throw new ChainUtilsRuntimeException("io exception", e);
		}
		return Base64Encoder.encodeFromBytes(encryptedData);
	}

	/**
	 * 使用公钥KEY来加密字符串
	 * 
	 * @param cryptoSrc
	 *            原字符串
	 * @return 解密的字符串
	 */
	public String encryptByPublicKey(String cryptoSrc) {
		/** 得到Cipher对象来实现对源数据的RSA加密 */
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			logger.error("no such exception", e);
			throw new ChainUtilsRuntimeException("no such exception", e);
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		} catch (InvalidKeyException e) {
			logger.error("invalid key exception", e);
			throw new ChainUtilsRuntimeException("invalid key exception", e);
		}
		byte[] data = cryptoSrc.getBytes();
		/** 执行分组加密操作 */
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache = null;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				try {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} catch (IllegalBlockSizeException | BadPaddingException e) {
					logger.error("exception", e);
					throw new ChainUtilsRuntimeException("exception", e);
				}
			} else {
				try {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				} catch (IllegalBlockSizeException | BadPaddingException e) {
					logger.error("exception", e);
					throw new ChainUtilsRuntimeException("exception", e);
				}
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		try {
			out.close();
		} catch (IOException e) {
			logger.error("io exception", e);
			throw new ChainUtilsRuntimeException("io exception", e);
		}

		return Base64Encoder.encodeFromBytes(encryptedData);
	}

	/**
	 * 将给定的公钥字符串转换为公钥对象
	 * 
	 * @param publicKeyStr
	 *            公钥字符串
	 * @return 公钥对象
	 */
	private static Key generatePublicKey(String publicKeyStr) {
		String publicKeyString = publicKeyStr;
		try {
			byte[] buffer = Base64Decoder.decodeToBytes(publicKeyString);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			Key publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException e) {
			logger.error("no such algorithm exception", e);
			throw new ChainUtilsRuntimeException("no such algorithm exception", e);
		} catch (InvalidKeySpecException e) {
			logger.error("invalid key spec exception", e);
			throw new ChainUtilsRuntimeException("invalid key spec exception", e);
		} catch (NullPointerException e) {
			logger.error("null pointer exception", e);
			throw new ChainUtilsRuntimeException("null pointer exception", e);
		} catch (Exception e) {
			logger.error("exception", e);
			throw new ChainUtilsRuntimeException("exception", e);
		}
	}

	/**
	 * 将给定的私钥字符串转换为私钥对象
	 * 
	 * @param privateKeyStr
	 *            私钥字符串
	 * @return 返回
	 */
	private static Key generatePrivateKey(String privateKeyStr) {
		String privateKeyString = privateKeyStr;
		try {
			byte[] buffer = Base64Decoder.decodeToBytes(privateKeyString);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			Key privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
			return privateKey;
		} catch (NoSuchAlgorithmException e) {
			logger.error("no such algorithm exception", e);
			throw new ChainUtilsRuntimeException("no such algorithm exception", e);
		} catch (InvalidKeySpecException e) {
			logger.error("invalid key spec exception", e);
			throw new ChainUtilsRuntimeException("invalid key spec exception", e);
		} catch (NullPointerException e) {
			logger.error("null pointer exception", e);
			throw new ChainUtilsRuntimeException("null pointer exception", e);
		} catch (Exception e) {
			logger.error("exception", e);
			throw new ChainUtilsRuntimeException("exception", e);
		}
	}

	/**
	 * 返回公钥字符串，若没有先set则返回null
	 * 
	 * @return 公钥字符串
	 */
	public String getPublicKeyString() {
		return publicKeyString;
	}

	/**
	 * 返回私钥字符串，若没有先set则返回null
	 * 
	 * @return 私钥字符串
	 */
	public String getPrivateKeyString() {
		return privateKeyString;
	}

	/**
	 * 返回私钥，若没有先set则返回null
	 * 
	 * @return 私钥Key
	 */
	public Key getPrivateKey() {
		return privateKey;
	}

	/**
	 * 返回公钥，若没有先set则返回null
	 * 
	 * @return 公钥Key
	 */
	public Key getPublicKey() {
		return publicKey;
	}

	/**
	 * 将KEY转为字符串形式
	 * 
	 * @param key
	 *            Key对象
	 * @return Key字符串形式
	 */
	public static String toKeyString(Key key) {
		return new String(Base64Encoder.encodeFromBytes(key.getEncoded()));
	}

	private RSAUtils() {

	}

	/**
	 * 由公钥加密
	 * 
	 * @param cryptoSrc
	 *            明文
	 * @param publicKey
	 *            公钥
	 * @return 密文
	 */
	public static String encryptByPublicKey(String cryptoSrc, Key publicKey) {
		RSAUtils rsa = new RSAUtils();
		rsa.publicKey = publicKey;
		rsa.publicKeyString = toKeyString(publicKey);
		return rsa.encryptByPublicKey(cryptoSrc);
	}

	/**
	 * 由公钥解密
	 * 
	 * @param cryptoSrc
	 *            密文
	 * @param publicKey
	 *            公钥
	 * @return 明文
	 */
	public static String decryptByPublicKey(String cryptoSrc, Key publicKey) {
		RSAUtils rsa = new RSAUtils();
		rsa.publicKey = publicKey;
		rsa.publicKeyString = toKeyString(publicKey);
		return rsa.decryptByPublicKey(cryptoSrc);
	}

	/**
	 * 由私钥加密
	 * 
	 * @param cryptoSrc
	 *            明文
	 * @param privateKey
	 *            私钥
	 * @return 密文
	 */
	public static String encryptByPrivateKey(String cryptoSrc, Key privateKey) {
		RSAUtils rsa = new RSAUtils();
		rsa.privateKey = privateKey;
		rsa.privateKeyString = toKeyString(privateKey);
		return rsa.encryptByPrivateKey(cryptoSrc);
	}

	/**
	 * 由私钥解密
	 * 
	 * @param cryptoSrc
	 *            密文
	 * @param privateKey
	 *            私钥
	 * @return 明文
	 */
	public static String decryptByPrivateKey(String cryptoSrc, Key privateKey) {
		RSAUtils rsa = new RSAUtils();
		rsa.privateKey = privateKey;
		rsa.privateKeyString = toKeyString(privateKey);
		return rsa.decryptByPrivateKey(cryptoSrc);
	}

}
