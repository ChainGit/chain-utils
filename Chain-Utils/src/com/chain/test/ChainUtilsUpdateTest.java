package com.chain.test;

import java.io.IOException;

import org.junit.Test;

import com.chain.update.ChainUtilsUpdate;

public class ChainUtilsUpdateTest {

	private static final String out = "E:/Temps/";

	@Test
	public void test() throws IOException {
		ChainUtilsUpdate.update(out);
	}

}
