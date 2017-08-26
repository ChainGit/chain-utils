package com.chain.test;

import java.io.IOException;

import org.junit.Test;

import com.chain.update.ChainUtilsUpdate;

public class ChainUtilsUpdateTest {

	private static final String out = "E:/Temps/";

	// @Test
	public void update() throws IOException {
		ChainUtilsUpdate.update(out);
	}

	@Test
	public void deploy() throws Exception {
		String dir = "E:\\Others\\Documents\\OneDrive\\博客\\source\\uploads\\chain-utils\\";
		// System.out.println(dir);
		ChainUtilsUpdate.deploy(dir);
		System.out.println("deploy done.");
	}

}
