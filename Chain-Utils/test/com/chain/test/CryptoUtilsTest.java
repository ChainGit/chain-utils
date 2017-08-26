package com.chain.test;

import java.security.Key;

import org.junit.Test;

import com.chain.utils.crypto.AESUtils;
import com.chain.utils.crypto.Base64Decoder;
import com.chain.utils.crypto.Base64Encoder;
import com.chain.utils.crypto.CryptoFactoryBean;
import com.chain.utils.crypto.DESUtils;
import com.chain.utils.crypto.MD5Utils;
import com.chain.utils.crypto.RSAUtils;

public class CryptoUtilsTest {

	private static final String text = "ABCD1234";

	private static final String KEY = "123456";

	// MD5测试
	@Test
	public void test1() {
		String enstr = MD5Utils.encrypt(text);
		System.out.println(enstr);
		System.out.println(enstr.length());
	}

	// DES测试
	@Test
	public void test2() {
		DESUtils des = new DESUtils("1", "A", "a");
		String enstr = des.encrypt(text);
		System.out.println(enstr);
		String destr = des.decrypt(enstr);
		System.out.println(destr);

		enstr = DESUtils.encrypt(text, "1", "A", "a");
		System.out.println(enstr);
		destr = DESUtils.decrypt(enstr, "1", "A", "a");
		System.out.println(destr);
	}

	// Base64测试
	@Test
	public void test3() {
		String enstr = Base64Encoder.encode(text);
		System.out.println(enstr);
		String destr = Base64Decoder.decode(enstr);
		System.out.println(destr);
	}

	// AES测试
	@Test
	public void test4() {
		AESUtils aes = new AESUtils(KEY);
		String enstr = aes.encrypt(text);
		System.out.println(enstr);
		String destr = aes.decrypt(enstr);
		System.out.println(destr);

		enstr = AESUtils.encrypt(text, KEY);
		System.out.println(enstr);
		destr = AESUtils.decrypt(enstr, KEY);
		System.out.println(destr);
	}

	// RSA测试
	@Test
	public void test5() throws Exception {
		String[] keys = RSAUtils.generateRandomKeyPairString();
		String publicKeyString = keys[0];
		String privateKeyString = keys[1];
		RSAUtils rsa = new RSAUtils(publicKeyString, privateKeyString);

		// System.out.println(publickey);
		// System.out.println(privatekey);

		String enstr1 = rsa.encryptByPublicKey(text);
		System.out.println(enstr1);
		String destr1 = rsa.decryptByPrivateKey(enstr1);
		System.out.println(destr1);

		String enstr2 = rsa.encryptByPrivateKey(text);
		System.out.println(enstr2);
		String destr2 = rsa.decryptByPublicKey(enstr2);
		System.out.println(destr2);

		Key[] keys2 = RSAUtils.generateKeyPair(publicKeyString, privateKeyString);
		Key publickey = keys2[0];
		Key privatekey = keys2[1];

		enstr1 = RSAUtils.encryptByPublicKey(text, publickey);
		System.out.println(enstr1);
		destr1 = RSAUtils.decryptByPrivateKey(enstr1, privatekey);
		System.out.println(destr1);

		enstr2 = RSAUtils.encryptByPrivateKey(text, privatekey);
		System.out.println(enstr2);
		destr2 = RSAUtils.decryptByPublicKey(enstr2, publickey);
		System.out.println(destr2);

		// RSA时公钥加密，私钥解密；反过来，私钥加密，公钥解密
		/*
		 * String enstr3 = rsa.encryptByPublicKey(text); System.out.println(enstr3);
		 * String destr3 = rsa.decryptByPublicKey(enstr3); System.out.println(destr3);
		 * 
		 * String enstr4 = rsa.encryptByPublicKey(text); System.out.println(enstr4);
		 * String destr4 = rsa.decryptByPublicKey(enstr4); System.out.println(destr4);
		 */
	}

	// 测试CryptoFactoryBean
	@Test
	public void test6() {
		String tmp = "E:/Temps/";
		CryptoFactoryBean factoryBean = new CryptoFactoryBean();
		factoryBean.setRsaPublicKeyFilePath(tmp + "public.key");
		factoryBean.setRsaPrivateKeyFilePath(tmp + "private.key");
		factoryBean.setDesFirstKeyFilePath(tmp + "des1.key");
		factoryBean.setDesSecondKeyFilePath(tmp + "des2.key");
		factoryBean.setDesThirdKeyFilePath(tmp + "des3.key");
		factoryBean.setAesKeyFilePath(tmp + "aes.key");

		RSAUtils rsa = factoryBean.getRsaUtils(true);
		AESUtils aes = factoryBean.getAesUtils(true);
		DESUtils des = factoryBean.getDesUtils(true);

		// String publicModulusString = rsa.getPublicModulusString();
		// String publicExponentString = rsa.getPublicExponentString();
		// System.out.println(publicModulusString);
		// System.out.println(publicExponentString);

		String enstr1 = rsa.encryptByPublicKey(text);
		System.out.println(enstr1);
		String destr1 = rsa.decryptByPrivateKey(enstr1);
		System.out.println(destr1);

		// 验证是否加密结果一致
		enstr1 = rsa.encryptByPublicKey(text);
		System.out.println(enstr1);
		destr1 = rsa.decryptByPrivateKey(enstr1);
		System.out.println(destr1);

		String enstr2 = rsa.encryptByPrivateKey(text);
		System.out.println(enstr2);
		String destr2 = rsa.decryptByPublicKey(enstr2);
		System.out.println(destr2);

		String enstr3 = des.encrypt(text);
		System.out.println(enstr3);
		String destr3 = des.decrypt(enstr3);
		System.out.println(destr3);

		String enstr4 = aes.encrypt(text);
		System.out.println(enstr4);
		String destr4 = aes.decrypt(enstr4);
		System.out.println(destr4);
	}

}
