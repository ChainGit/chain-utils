package com.chain.utils.crypto;

/**
 *  LICENSE AND TRADEMARK NOTICES
 *  
 *  Except where noted, sample source code written by Motorola Mobility Inc. and
 *  provided to you is licensed as described below.
 *  
 *  Copyright (c) 2012, Motorola, Inc.
 *  All  rights reserved except as otherwise explicitly indicated.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  - Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *
 *  - Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 *  - Neither the name of Motorola, Inc. nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 *  
 *  Other source code displayed may be licensed under Apache License, Version
 *  2.
 *  
 *  Copyright ¬© 2012, Android Open Source Project. All rights reserved unless
 *  otherwise explicitly indicated.
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy
 *  of the License at
 *  
 *  http://www.apache.org/licenses/LICENSE-2.0.
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 *  
 */

// Please refer to the accompanying article at 
// http://developer.motorola.com/docs/using_the_advanced_encryption_standard_in_android/
// A tutorial guide to using AES encryption in Android
// First we generate a 256 bit secret key; then we use that secret key to AES encrypt a plaintext message.
// Finally we decrypt the ciphertext to get our original message back.
// We don't keep a copy of the secret key - we generate the secret key whenever it is needed, 
// so we must remember all the parameters needed to generate it -
// the salt, the IV, the human-friendly passphrase, all the algorithms and parameters to those algorithms.
// Peter van der Linden, April 15 2012

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;

import com.chain.exception.ChainUtilsRuntimeException;
import com.chain.logging.ChainUtilsLoggerFactory;

/**
 * AES加解密
 * 
 * 依赖架包下载： https://www.bouncycastle.org/latest_releases.html
 * 
 * @author Collected
 * @version 1.1
 * 
 */
public class AESUtils {

	private static final Logger logger = ChainUtilsLoggerFactory.getLogger(AESUtils.class);

	private static final String KEY_GENERATION_ALG = "PBKDF2WithHmacSHA1"; // PBEWITHSHAANDTWOFISH-CBC
	private static final String CIPHERMODEPADDING = "AES/CBC/PKCS7Padding";

	private static final String ENCODING = "UTF-8";

	private static final int HASH_ITERATIONS = 10000;
	private static final int KEY_LENGTH = 128;

	private static final byte[] SALT = { 106, 105, 106, 105, 97, 0x1, 0x5, 0x9, 0x9, 0x6, 0x3, 0x7, 0x3, 0x3, 0x9,
			0x0 }; // must

	private static final String IV_DEFAULT_STR = "0123456789123123";// 16位，如果不设置iv则使用默认的

	private static SecretKeyFactory keyfactory = null;

	private SecretKey sk = null;
	private SecretKeySpec skforAES = null;

	private IvParameterSpec iv;
	private PBEKeySpec myKeyspec = null;

	static {
		if (keyfactory == null)
			try {
				keyfactory = SecretKeyFactory.getInstance(KEY_GENERATION_ALG);
			} catch (NoSuchAlgorithmException e) {
				logger.error("AES keyfactory get instance exception", e);
				// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
				throw new ChainUtilsRuntimeException("AES keyfactory get instance exception", e);
			}
	}

	public AESUtils(String passphrase) {
		init(passphrase, IV_DEFAULT_STR);
	}

	public AESUtils(String passphrase, String ivStr) {
		init(passphrase, ivStr);
	}

	/**
	 * 初始化
	 * 
	 * @param passphrase
	 *            密码
	 */
	private void init(String passphrase, String ivStr) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		if (passphrase == null || passphrase.length() < 1) {
			throw new ChainUtilsRuntimeException("passphrase can't be null or empty.");
		}

		try {
			myKeyspec = new PBEKeySpec(passphrase.toCharArray(), SALT, HASH_ITERATIONS, KEY_LENGTH);
			sk = keyfactory.generateSecret(myKeyspec);
			byte[] skAsByteArray = sk.getEncoded();
			skforAES = new SecretKeySpec(skAsByteArray, "AES");
			iv = new IvParameterSpec(ivStr.getBytes());
		} catch (InvalidKeySpecException ikse) {
			logger.error("invalid key spec for PBEWITHSHAANDTWOFISH-CBC", ikse);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("invalid key spec for PBEWITHSHAANDTWOFISH-CBC", ikse);
		}
	}

	/**
	 * AES加密
	 * 
	 * @param plaintext
	 *            要加密的字符串
	 * @return 加密字符串
	 */
	public String encrypt(String plaintext) {
		byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, iv, plaintext);
		String base64_ciphertext = Base64Encoder.encodeFromBytes(ciphertext);
		return base64_ciphertext;
	}

	/**
	 * 
	 * AES解密
	 * 
	 * @param ciphertext_base64
	 *            Base64编码的加密字符串
	 * @return 解密字符串
	 */
	public String decrypt(String ciphertext_base64) {
		byte[] s = Base64Decoder.decodeToBytes(ciphertext_base64);
		String decrypted = null;
		try {
			decrypted = new String(decrypt(CIPHERMODEPADDING, skforAES, iv, s), ENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.error("unspported encoding exception", e);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("unspported encoding exception", e);
		}
		return decrypted;
	}

	private static byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec iv, String msg) {
		try {
			Cipher c = Cipher.getInstance(cmp);
			c.init(Cipher.ENCRYPT_MODE, sk, iv);
			return c.doFinal(msg.getBytes(ENCODING));
		} catch (NoSuchAlgorithmException nsae) {
			logger.error("no cipher getinstance support for " + cmp, nsae);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("no cipher getinstance support for " + cmp, nsae);
		} catch (NoSuchPaddingException nspe) {
			logger.error("no cipher getinstance support for padding " + cmp, nspe);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("no cipher getinstance support for " + cmp, nspe);
		} catch (InvalidKeyException e) {
			logger.error("invalid key exception", e);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("invalid key exception", e);
		} catch (InvalidAlgorithmParameterException e) {
			logger.error("invalid algorithm parameter exception", e);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("invalid algorithm parameter exception", e);
		} catch (IllegalBlockSizeException e) {
			logger.error("illegal block size exception", e);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("illegal block size exception", e);
		} catch (BadPaddingException e) {
			logger.error("bad padding exception", e);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("bad padding exception", e);
		} catch (UnsupportedEncodingException e) {
			logger.error("unsupported Encoding Exception", e);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("unsupported Encoding Exception", e);
		}
	}

	private static byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec iv, byte[] ciphertext) {
		try {
			Cipher c = Cipher.getInstance(cmp);
			c.init(Cipher.DECRYPT_MODE, sk, iv);
			return c.doFinal(ciphertext);
		} catch (NoSuchAlgorithmException nsae) {
			logger.error("no cipher getinstance support for " + cmp, nsae);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("no cipher getinstance support for " + cmp, nsae);
		} catch (NoSuchPaddingException nspe) {
			logger.error("no cipher getinstance support for padding " + cmp, nspe);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("no cipher getinstance support for " + cmp, nspe);
		} catch (InvalidKeyException e) {
			logger.error("invalid key exception", e);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("invalid key exception", e);
		} catch (InvalidAlgorithmParameterException e) {
			logger.error("invalid algorithm parameter exception", e);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("invalid algorithm parameter exception", e);
		} catch (IllegalBlockSizeException e) {
			logger.error("illegal block size exception", e);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("illegal block size exception", e);
		} catch (BadPaddingException e) {
			logger.error("bad padding exception", e);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("bad padding exception", e);
		}
	}

	/**
	 * 静态方法，加密字符串
	 * 
	 * @param plaintext
	 *            原字符串
	 * @param passphrase
	 *            密钥
	 * @return 加密并Base64的字符串
	 */
	public static String encrypt(String plaintext, String passphrase) {
		return encrypt(plaintext, passphrase, IV_DEFAULT_STR);
	}

	/**
	 * 静态方法，加密字符串
	 * 
	 * @param plaintext
	 *            原字符串
	 * @param passphrase
	 *            密钥
	 * @param ivStr
	 *            iv偏移量（16位）
	 * @return 加密并Base64的字符串
	 */
	public static String encrypt(String plaintext, String passphrase, String ivStr) {
		if (passphrase == null || passphrase.length() < 1) {
			throw new ChainUtilsRuntimeException("passphrase can't be null or empty.");
		}
		SecretKey sk = null;
		SecretKeySpec skforAES = null;
		IvParameterSpec iv = null;
		PBEKeySpec myKeyspec = null;
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		if (passphrase == null || passphrase.length() < 1) {
			throw new ChainUtilsRuntimeException("passphrase can't be null or empty.");
		}

		try {
			myKeyspec = new PBEKeySpec(passphrase.toCharArray(), SALT, HASH_ITERATIONS, KEY_LENGTH);
			sk = keyfactory.generateSecret(myKeyspec);
			byte[] skAsByteArray = sk.getEncoded();
			skforAES = new SecretKeySpec(skAsByteArray, "AES");
			iv = new IvParameterSpec(ivStr.getBytes());
		} catch (InvalidKeySpecException ikse) {
			logger.error("invalid key spec for PBEWITHSHAANDTWOFISH-CBC", ikse);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("invalid key spec for PBEWITHSHAANDTWOFISH-CBC", ikse);
		}
		byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, iv, plaintext);
		String base64_ciphertext = Base64Encoder.encodeFromBytes(ciphertext);
		return base64_ciphertext;
	}

	/**
	 * 静态方法，解密字符串
	 * 
	 * @param ciphertext_base64
	 *            Base64加密的字符串
	 * @param passphrase
	 *            密钥
	 * @return 解密的字符串
	 */
	public static String decrypt(String ciphertext_base64, String passphrase) {
		return decrypt(ciphertext_base64, passphrase, IV_DEFAULT_STR);
	}

	/**
	 * 静态方法，解密字符串
	 * 
	 * @param ciphertext_base64
	 *            Base64加密的字符串
	 * @param passphrase
	 *            密钥
	 * @param ivStr
	 *            iv偏移量（16位）
	 * @return 解密的字符串
	 */
	public static String decrypt(String ciphertext_base64, String passphrase, String ivStr) {
		if (passphrase == null || passphrase.length() < 1) {
			throw new ChainUtilsRuntimeException("passphrase can't be null or empty.");
		}
		SecretKey sk = null;
		SecretKeySpec skforAES = null;
		IvParameterSpec iv = null;
		PBEKeySpec myKeyspec = null;
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		if (passphrase == null || passphrase.length() < 1) {
			throw new ChainUtilsRuntimeException("passphrase can't be null or empty.");
		}

		try {
			myKeyspec = new PBEKeySpec(passphrase.toCharArray(), SALT, HASH_ITERATIONS, KEY_LENGTH);
			sk = keyfactory.generateSecret(myKeyspec);
			byte[] skAsByteArray = sk.getEncoded();
			skforAES = new SecretKeySpec(skAsByteArray, "AES");
			iv = new IvParameterSpec(ivStr.getBytes());
		} catch (InvalidKeySpecException ikse) {
			logger.error("invalid key spec for PBEWITHSHAANDTWOFISH-CBC", ikse);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("invalid key spec for PBEWITHSHAANDTWOFISH-CBC", ikse);
		}
		byte[] s = Base64Decoder.decodeToBytes(ciphertext_base64);
		String decrypted = null;
		try {
			decrypted = new String(decrypt(CIPHERMODEPADDING, skforAES, iv, s), ENCODING);
		} catch (UnsupportedEncodingException e) {
			logger.error("unspported encoding exception", e);
			// 按照标准使用API一般能正确生成实例，所以这里转为RuntimeException
			throw new ChainUtilsRuntimeException("unspported encoding exception", e);
		}
		return decrypted;
	}

	/**
	 * 返回新的实例
	 * 
	 * @param passphrase
	 *            密码
	 * @return 实例
	 */
	public static AESUtils getInstance(String passphrase) {
		return new AESUtils(passphrase);
	}

}