package com.chain.test;

import java.io.File;

import org.junit.Test;

import com.chain.update.ChainUtilsUpdate;

public class ChainUtilsUpdateTest {

	@Test
	public void deploy() throws Exception {
		String base = System.getProperty("user.dir");
		String dir = base + File.separator + "web";
		// System.out.println(dir);
		ChainUtilsUpdate.deploy(dir);
		System.out.println("deploy done.");
	}

}
