package com.chain.test;

import org.junit.Test;

import com.chain.utils.crypto.AESUtils;
import com.chain.utils.crypto.Base64Decoder;
import com.chain.utils.crypto.Base64Encoder;
import com.chain.utils.crypto.DESUtils;
import com.chain.utils.crypto.MD5Utils;
import com.chain.utils.crypto.RSAUtils;

public class CryptoUtilsTest {

	private static final String text = "ABCD1234";

	// MD5测试
	@Test
	public void test1() {
		MD5Utils md5 = new MD5Utils();
		String enstr = md5.encode(text);
		System.out.println(enstr);
		System.out.println(enstr.length());
	}

	// DES测试
	@Test
	public void test2() {
		DESUtils des = new DESUtils();
		String enstr = des.encode(text, "1", "A", "a");
		System.out.println(enstr);
		String destr = des.decode(enstr, "1", "A", "a");
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
		AESUtils aes = new AESUtils();
		String enstr = aes.encrypt(text, "123456");
		System.out.println(enstr);
		String destr = aes.decrypt(enstr, "123456");
		System.out.println(destr);
	}

	// RSA测试
	@Test
	public void test5() throws Exception {
		RSAUtils rsa = new RSAUtils();
		String[] keys = RSAUtils.generateRandomKeyPairString();
		String publickey = keys[0];
		String privatekey = keys[1];

		// System.out.println(publickey);
		// System.out.println(privatekey);

		String enstr1 = rsa.encryptByPublicKeyString(text, publickey);
		System.out.println(enstr1);
		String destr1 = rsa.decryptByPrivateKeyString(enstr1, privatekey);
		System.out.println(destr1);

		String enstr2 = rsa.encryptByPrivateKeyString(text, privatekey);
		System.out.println(enstr2);
		String destr2 = rsa.decryptByPublicKeyString(enstr2, publickey);
		System.out.println(destr2);
	}

}
