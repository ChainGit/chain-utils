package com.chain.test;

import org.junit.Test;

import com.chain.exception.ChainUtilsException;
import com.chain.utils.others.PinyinUtils;

public class PinyinUtilsTest {

	@Test
	public void test() throws ChainUtilsException {
		String s = "A我#是123中 国人1";
		String p = PinyinUtils.getPinyin(s);
		System.out.println(p);
	}

}
