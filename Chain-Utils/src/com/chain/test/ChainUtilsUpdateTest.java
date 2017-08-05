package com.chain.test;

import java.io.IOException;

import org.junit.Test;

import com.chain.update.ChainUtilsUpdate;
import com.chain.utils.crypto.AESUtils;

public class ChainUtilsUpdateTest {

	private static final String out = "E:/Temps/";

	private static final String DEPLOY_DIR = "Rp+PHeSm3IvN+TlM1Jx4aBwOIsXWiT4lj6GghfROVgIJDU/eJ++nRDxWWbqmKzYu3KHJDn57liu8\r\n"
			+ "r8VEWIb+Sw==";

	private static final String KEY = "123456890";

	// @Test
	public void update() throws IOException {
		ChainUtilsUpdate.update(out);
	}

	@Test
	public void deploy() throws IOException {
		String dir = AESUtils.getInstance().decrypt(DEPLOY_DIR, KEY);
		// System.out.println(dir);
		ChainUtilsUpdate.deploy(dir);
		System.out.println("deploy done.");
	}

}
