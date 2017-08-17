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

		String enstr1 = rsa.encryptByPublicKey("{\"t\":\"123\"}");
		System.out.println(enstr1);
		String destr1 = rsa.decryptByPrivateKey(enstr1);
		System.out.println(destr1);

		String enstr2 = rsa.encryptByPrivateKey(text);
		System.out.println(enstr2);
		String destr2 = rsa
				.decryptByPublicKey("qThHD55Wt2TZRPbuRHcgoV4MhfhOL3NZHCVtRYh+otyo1b8BsTCujwezn4GzVc9qAKl6iYdhf5MH\r\n"
						+ "aVcfjPGBjXwOp3qvD+f/c8E0z18x58OnJh042bExhQMdm9t3QVaFzOp9i/galg81ICrP49YObqc2\r\n"
						+ "5Rc5uYapM3YcuMLCkk0YxveVs1iAVwEl6jXDm/T/R61d9I2PE6nvcumzUIJD9CW8TN0eoof9en9b\r\n"
						+ "+AzqaVdPL8JOlIxrqPuDpNlyfD+G2w9LNYSbThHhPaYq2OkYLxSc91xHy36NlB6RoBIJS2XaXINf\r\n"
						+ "pMOXJ1JyvcytuFQqCM6UudxPZ52rfibPS4IcLLQ+JOYGbtbsbheZry8iLTm7Z3V1uXq/jBCEJK6G\r\n"
						+ "u3d5MdczKRvjwkVeTViqbahElRGx4fuwqErX+sGGic1CGp+BjJarzIFWECebkaqmWBo98uI8zwAW\r\n"
						+ "V+dEl9D5tv14sCU0cBBeZGXS/Fn4/NXHrY0xDauqmKduoz50jTVmHo0o");
		System.out.println(destr2);
		destr2 = rsa
				.decryptByPublicKey("Rv+jbt5buRPMoHn8a3ocRhEYPrICPDdi2IDSUMNnhw29AuaJeXxBkAQ6t74WQSKxHxPXIbS1P+hS\r\n"
						+ "CMvGl76G2Z80qKiGyhE9zkQYwnfOSmGbzdqEsbPH6VSe6kB1m7hbEhfydSfbA4hgUK1HdXRnp51Y\r\n"
						+ "wemRNlSNPRTNIZS+IVY=");
		System.out.println(destr2);
		destr2 = rsa
				.decryptByPublicKey("McN2/Syl0vLUQfkLBKeOQvLCiI//0I9lrtkLrFz7LJNT6eS8r5PZ36xAF9h1o9fSDu2B1daK3hcm\r\n"
						+ "DpcOa7Bnx1VVRDu3lW/tSLkGsJz1AFZbPukwimYfn5rM//Ppm6E3ZwL8fbE6UB/bRFSi2+UIl7UL\r\n"
						+ "37FQW2F12/JJsPOoFCt1SKA6tX7UdMJumfqE3E1g4X3O3QGNQDFoOXZ5L5bu6gDEa8tWlVzhBS6E\r\n"
						+ "fHCzw1Ycej/KfwZ54R8Dr8I8AWiyBkroCnbYKf0t02UCzMg/cLbBY01SR7POu+Xdrpxus8NfP1R5\r\n"
						+ "t8O9i/coc2SoigQmf+VGV0dJdr/VdKvYYw6B9Q==");
		System.out.println(destr2);
		destr2 = rsa
				.decryptByPublicKey("EjEvzxdraKYuXq5Pa/j5VmfU4YHT9cvkupayc2oKwaAAaFl8WN687rJOsUyAOHgPUskK0+sccAGr\r\n" + 
						"wmsJLq2wJxP2If0suEH5CZsLlHqaSyYPuAEDpRtZxBrnitj5s7PfGSJwdJ5uT7cOVYrOytoinRDY\r\n" + 
						"8HnUXS02xLI0B3OjITYUHEPLDwxtXhjJuSMGmMNOcvUjuzdxwHY9iYXZaPvzmwvRWjV/b7hRjyuW\r\n" + 
						"QgvxJEmccvE8Va06wtnD284Yyg+KwGeMUe10pS12OpaM2gxoKGMea/ttL4l9Sv1SgqJ1wnOvoRgZ\r\n" + 
						"Tj+ZFfg7OrrCIRvV1J+9TRApZqGsC115Li16xVZv1SxoOHngKPORtc9DqkuspA1ej/fSfFxpe6+s\r\n" + 
						"VXNHAJfQJtt1joO4Y0yBidmBg4YQs2ZJ6oNSwUEsAxz1dVmIiOLg6vuK0ez/ulGmo8XKNvYi9ccv\r\n" + 
						"PiSbdSfOCpkhE+QA1+7Y3Px/6bbdpP1bF7SMSsytTzzTIPwvnbITbFx1VgNreOLJv1ICTzacbPco\r\n" + 
						"AnKqO8sNTDXOBAMp9hDExtn9/AwLWxwe7VkjgXAeWugeZS6fHD7I+609cNunR20z1XUN7sWuo/sb\r\n" + 
						"SGhIkt75LCGFDNoFvM5r3V43BkP6bdu0q6XNW1WTIw2zm1WUniwjXZq9jdjMjfhbXLyohOmDFbg=");
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
