package com.chain.utils.crypto;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import javax.crypto.Cipher;

/**
 * 非对称加密算法RSA
 * 
 * @author Collected
 * @version 1.0
 * 
 * 
 */
public class RSAUtils {
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
	/** 指定字符集 */
	private static final String CHARSET = "UTF-8";

	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;
	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * 生成随机的公钥和私钥KEY密钥对，公钥KEY为第一个，私钥KEY为第二个
	 * 
	 * @throws Exception
	 *             异常
	 * @return 密钥对
	 */
	public static Key[] generateRandomKeyPair() throws Exception {
		// /** RSA算法要求有一个可信任的随机数源 */
		SecureRandom secureRandom = new SecureRandom();
		/** 为RSA算法创建一个KeyPairGenerator对象 */
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
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
	 * @throws Exception
	 *             异常
	 */
	public static String[] generateRandomKeyPairString() throws Exception {
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
	 * @throws Exception
	 *             异常
	 */
	public static String[] generateKeyPairString(String publicKeyStr, String privateKeyStr) throws Exception {
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
	 * @throws Exception
	 *             异常
	 */
	public static Key[] generateKeyPair(String publicKeyStr, String privateKeyStr) throws Exception {
		Key[] keys = new Key[2];
		keys[0] = generatePublicKey(publicKeyStr);
		keys[1] = generatePrivateKey(privateKeyStr);
		return keys;
	}

	/**
	 * 生成公钥KEY对象
	 * 
	 * @param publicKeyStr
	 *            公钥字符串
	 * @throws Exception
	 *             异常
	 */
	public void setPublicKey(String publicKeyStr) throws Exception {
		publicKey = generatePublicKey(publicKeyStr);
	}

	/**
	 * 生成私钥KEY对象
	 * 
	 * @param privateKeyStr
	 *            私钥字符串
	 * @throws Exception
	 *             异常
	 */
	public void setPrivateKey(String privateKeyStr) throws Exception {
		privateKey = generatePrivateKey(privateKeyStr);
	}

	/**
	 * 从InputStream中加载公钥文件
	 * 
	 * @param in
	 *            公钥输入流
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public void setPublicKey(InputStream in) throws Exception {
		try {
			publicKey = generatePublicKey(readKey(in));
		} catch (IOException e) {
			throw new Exception("公钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥输入流为空");
		}
	}

	/**
	 * 从InputStream中加载私钥文件
	 * 
	 * @param in
	 *            输入流
	 * @throws Exception
	 *             异常
	 */
	public void setPrivateKey(InputStream in) throws Exception {
		try {
			privateKey = generatePrivateKey(readKey(in));
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		}
	}

	/**
	 * 从InputStream中读取密钥字符串
	 * 
	 * @param in
	 *            输入流
	 * @return 密钥字符串
	 * @throws IOException
	 *             异常
	 */
	private String readKey(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String readLine = null;
		StringBuilder sb = new StringBuilder();
		while ((readLine = br.readLine()) != null) {
			if (readLine.charAt(0) == '-') {
				continue;
			} else {
				sb.append(readLine);
				sb.append('\r');
			}
		}

		return sb.toString();
	}

	/**
	 * 使用私钥KEY字符串加密
	 * 
	 * @param cryptoSrc
	 *            原字符串
	 * @param privatekey
	 *            私钥
	 * @return 加密的字符串
	 * @throws Exception
	 *             异常
	 */
	public String encryptByPrivateKeyString(String cryptoSrc, String privatekey) throws Exception {
		return encryptByPrivateKey(cryptoSrc, generatePrivateKey(privatekey));
	}

	/**
	 * 使用公钥KEY字符串解密
	 * 
	 * @param cryptoSrc
	 *            加密的字符串
	 * @param publickey
	 *            公钥
	 * @return 解密的字符串
	 * @throws Exception
	 *             异常
	 */
	public String decryptByPublicKeyString(String cryptoSrc, String publickey) throws Exception {
		return decryptByPublicKey(cryptoSrc, generatePublicKey(publickey));
	}

	/**
	 * 使用公钥KEY字符串加密
	 * 
	 * @param cryptoSrc
	 *            原字符串
	 * @param publickey
	 *            公钥
	 * @return 加密的字符串
	 * @throws Exception
	 *             异常
	 */
	public String encryptByPublicKeyString(String cryptoSrc, String publickey) throws Exception {
		return encryptByPublicKey(cryptoSrc, generatePublicKey(publickey));
	}

	/**
	 * 使用私钥KEY字符串解密
	 * 
	 * @param cryptoSrc
	 *            加密的字符串
	 * @param privatekey
	 *            私钥
	 * @return 解密的字符串
	 * @throws Exception
	 *             异常
	 */
	public String decryptByPrivateKeyString(String cryptoSrc, String privatekey) throws Exception {
		return decryptByPrivateKey(cryptoSrc, generatePrivateKey(privatekey));
	}

	/**
	 * 使用私钥KEY来解密
	 * 
	 * @param cryptoSrc
	 *            加密的字符串
	 * @param privatekey
	 *            私钥
	 * @return 解密的字符串
	 * @throws Exception
	 *             异常
	 */
	public String decryptByPrivateKey(String cryptoSrc, Key privatekey) throws Exception {
		/** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		// Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privatekey);
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
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return new String(decryptedData);
	}

	/**
	 * 使用公钥KEY来解密
	 * 
	 * @param cryptoSrc
	 *            解密的字符串
	 * @param publickey
	 *            公钥
	 * @return 解密后的字符串
	 * @throws Exception
	 *             异常
	 */
	public String decryptByPublicKey(String cryptoSrc, Key publickey) throws Exception {
		/** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
		// Cipher cipher = Cipher.getInstance(ALGORITHM);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, publickey);
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
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();

		return new String(decryptedData);
	}

	/**
	 * 使用私钥KEY来加密字符串
	 * 
	 * @param cryptoSrc
	 *            原字符串
	 * @param privatekey
	 *            私钥
	 * @return 加密的字符串
	 * @throws Exception
	 *             异常
	 */
	public String encryptByPrivateKey(String cryptoSrc, Key privatekey) throws Exception {
		/** 得到Cipher对象来实现对源数据的RSA加密 */
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, privatekey);
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
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return Base64Encoder.encode(encryptedData);
	}

	/**
	 * 使用公钥KEY来加密字符串
	 * 
	 * @param cryptoSrc
	 *            原字符串
	 * @param publickey
	 *            公钥
	 * @return 解密的字符串
	 * @throws Exception
	 *             异常
	 */
	public String encryptByPublicKey(String cryptoSrc, Key publickey) throws Exception {
		/** 得到Cipher对象来实现对源数据的RSA加密 */
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publickey);
		byte[] data = cryptoSrc.getBytes();
		/** 执行分组加密操作 */
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();

		return Base64Encoder.encode(encryptedData);
	}

	/**
	 * 将给定的公钥字符串转换为公钥对象
	 * 
	 * @param publicKeyStr
	 *            公钥字符串
	 * @return 公钥对象
	 * @throws Exception
	 *             异常
	 */
	private static Key generatePublicKey(String publicKeyStr) throws Exception {
		String publicKeyString = publicKeyStr;
		try {
			byte[] buffer = Base64Decoder.decodeToBytes(publicKeyString);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			Key publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		} catch (Exception e) {
			throw new Exception("公钥数据内容读取错误");
		}
	}

	/**
	 * 将给定的私钥字符串转换为私钥对象
	 * 
	 * @param privateKeyStr
	 *            私钥字符串
	 * @return 返回
	 * @throws Exception
	 *             异常
	 */
	private static Key generatePrivateKey(String privateKeyStr) throws Exception {
		String privateKeyString = privateKeyStr;
		try {
			byte[] buffer = Base64Decoder.decodeToBytes(privateKeyString);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			Key privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
			return privateKey;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		} catch (Exception e) {
			throw new Exception("私钥数据内容读取错误");
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
		return new String(Base64Encoder.encode(key.getEncoded()));
	}

}
