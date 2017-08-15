package com.chain.utils.crypto;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chain.exception.ChainUtilsRuntimeException;
import com.chain.utils.StringUtils;

/**
 * 常用需要密码的加密方法的汇总（DES,AES,RSA），也是一个FactoryBean，可注入Spring容器使用
 * 
 * @author Chain Qian
 * @version 1.0
 *
 */
public class CryptoFactoryBean {

	private static Logger logger = LoggerFactory.getLogger(CryptoFactoryBean.class);

	private Map<String, String> keyStore;

	public CryptoFactoryBean() {
		keyStore = new HashMap<>();
	}

	/**
	 * 检查字符串是否为空
	 * 
	 * @param keyStr
	 *            检查的字符串
	 */
	private void checkStr(String keyStr) {
		if (StringUtils.isEmpty(keyStr))
			throw new ChainUtilsRuntimeException("key is empty or null");
	}

	/**
	 * 从文件中读取内容
	 * 
	 * @param file
	 *            文件
	 * @return 文件中的内容
	 */
	private String readFromFile(String file) {
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			baos = new ByteArrayOutputStream();
			byte[] buf = new byte[8 * 1024];
			int len = -1;
			while ((len = bis.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			baos.flush();
			return baos.toString("UTF-8");
		} catch (IOException e) {
			logger.error("io exception", e);
			throw new ChainUtilsRuntimeException("io exception", e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					logger.error("io exception", e);
					throw new ChainUtilsRuntimeException("io exception", e);
				}
			}

			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					logger.error("io exception", e);
					throw new ChainUtilsRuntimeException("io exception", e);
				}
			}
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////

	public void setRsaPublicKeyFilePath(String keyFile) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(keyFile);
			String publicKey = RSAUtils.toKeyString(RSAUtils.loadPublicKey(is));
			setRsaPublicKeyString(publicKey);
		} catch (IOException e) {
			logger.error("io exception", e);
			throw new ChainUtilsRuntimeException("io exception", e);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					logger.error("io exception", e);
					throw new ChainUtilsRuntimeException("io exception", e);
				}
		}
	}

	public void setRsaPrivateKeyFilePath(String keyFile) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(keyFile);
			String privateKey = RSAUtils.toKeyString(RSAUtils.loadPrivateKey(is));
			setRsaPrivateKeyString(privateKey);
		} catch (IOException e) {
			logger.error("io exception", e);
			throw new ChainUtilsRuntimeException("io exception", e);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					logger.error("io exception", e);
					throw new ChainUtilsRuntimeException("io exception", e);
				}
		}
	}

	public void setRsaPrivateKeyString(String keyStr) {
		checkStr(keyStr);

		keyStore.put(RSA_PRIVATE_KEY, keyStr);
	}

	public void setRsaPublicKeyString(String keyStr) {
		checkStr(keyStr);

		keyStore.put(RSA_PUBLIC_KEY, keyStr);
	}

	public void setRsaPrivateKeyBase64EncodedString(String ekeyStr) {
		String keyStr = Base64Decoder.decode(ekeyStr);
		setRsaPrivateKeyString(keyStr);
	}

	public void setRsaPublicKeyBase64EncodedString(String ekeyStr) {
		String keyStr = Base64Decoder.decode(ekeyStr);
		setRsaPublicKeyString(keyStr);
	}

	//////////////////////////////////////////////////////////////////////////////////////

	public void setDesFirstKeyString(String key) {
		checkStr(key);
		keyStore.put(DES_FIRST_KEY, key);
	}

	public void setDesSecondKeyString(String key) {
		checkStr(key);
		keyStore.put(DES_SECOND_KEY, key);
	}

	public void setDesThirdKeyString(String key) {
		checkStr(key);
		keyStore.put(DES_THIRD_KEY, key);
	}

	public void setDesFirstKeyBase64EncodedString(String ekey) {
		String key = Base64Decoder.decode(ekey);
		setDesFirstKeyString(key);
	}

	public void setDesSecondKeyBase64EncodedString(String ekey) {
		String key = Base64Decoder.decode(ekey);
		setDesSecondKeyString(key);
	}

	public void setDesThirdKeyBase64EncodedString(String ekey) {
		String key = Base64Decoder.decode(ekey);
		setDesThirdKeyString(key);
	}

	public void setDesFirstKeyFilePath(String keyFile) {
		String key = readFromFile(keyFile);
		setDesFirstKeyString(key);
	}

	public void setDesSecondKeyFilePath(String keyFile) {
		String key = readFromFile(keyFile);
		setDesSecondKeyString(key);
	}

	public void setDesThirdKeyFilePath(String keyFile) {
		String key = readFromFile(keyFile);
		setDesThirdKeyString(key);
	}

	//////////////////////////////////////////////////////////////////////////////////////

	public void setAesKeyString(String key) {
		checkStr(key);
		keyStore.put(AES_KEY, key);
	}

	public void setAesKeyBase64EncodedString(String ekey) {
		String key = Base64Decoder.decode(ekey);
		setAesKeyString(key);
	}

	public void setAesKeyFilePath(String keyFile) {
		String key = readFromFile(keyFile);
		setAesKeyString(key);
	}

	//////////////////////////////////////////////////////////////////////////////////////

	private AESUtils aesUtils;
	private DESUtils desUtils;
	private RSAUtils rsaUtils;

	private static final String RSA_PUBLIC_KEY = "RsaPublicKey";
	private static final String RSA_PRIVATE_KEY = "RsaPrivateKey";
	private static final String DES_FIRST_KEY = "DesFirstKey";
	private static final String DES_SECOND_KEY = "DesSecondtKey";
	private static final String DES_THIRD_KEY = "DesThirdtKey";
	private static final String AES_KEY = "AesKey";

	/**
	 * 获取AES工具类
	 * 
	 * @param isSingleTon
	 *            是否是单例的
	 * @return 工具类实例
	 */
	public AESUtils getAesUtils(boolean isSingleTon) {
		String key = keyStore.get(AES_KEY);
		if (aesUtils == null)
			checkStr(key);
		if (!isSingleTon)
			return new AESUtils(key);
		else {
			if (aesUtils == null)
				aesUtils = new AESUtils(key);
			return aesUtils;
		}
	}

	/**
	 * 获取DES工具类
	 * 
	 * @param isSingleTon
	 *            是否是单例的
	 * @return 工具类实例
	 */
	public DESUtils getDesUtils(boolean isSingleTon) {
		String key1 = keyStore.get(DES_FIRST_KEY);
		String key2 = keyStore.get(DES_SECOND_KEY);
		String key3 = keyStore.get(DES_THIRD_KEY);
		if (desUtils == null)
			if (StringUtils.isEmpty(key1) && StringUtils.isEmpty(key2) && StringUtils.isEmpty(key3))
				throw new ChainUtilsRuntimeException("three keys must have at least one which is not empty or null");
		if (!isSingleTon)
			return new DESUtils(key1, key2, key3);
		else {
			if (desUtils == null)
				desUtils = new DESUtils(key1, key2, key3);
			return desUtils;
		}
	}

	/**
	 * 获取RSA工具类
	 * 
	 * @param isSingleTon
	 *            是否是单例的
	 * @return 工具类实例
	 */
	public RSAUtils getRsaUtils(boolean isSingleTon) {
		String publicKey = keyStore.get(RSA_PUBLIC_KEY);
		String privateKey = keyStore.get(RSA_PRIVATE_KEY);
		if (rsaUtils == null) {
			checkStr(publicKey);
			checkStr(privateKey);
		}
		if (!isSingleTon)
			return new RSAUtils(publicKey, privateKey);
		else {
			if (rsaUtils == null)
				rsaUtils = new RSAUtils(publicKey, privateKey);
			return rsaUtils;
		}
	}

}
