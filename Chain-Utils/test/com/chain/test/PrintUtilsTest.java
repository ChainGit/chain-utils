package com.chain.test;

import org.junit.Test;

import com.chain.exception.ChainUtilsRuntimeException;
import com.chain.utils.PrintUtils;

public class PrintUtilsTest {

	@Test
	public void test() {
		PrintUtils.show(123);
		throw new ChainUtilsRuntimeException("error");
	}

}
