package com.chain.test;

import org.junit.Test;

import com.chain.utils.RandomStringUtils;

public class RandomStringUtilsTest {

	@Test
	public void test() {
		String s1 = RandomStringUtils.generateZeroString(12);
		System.out.println(s1);
		String s2 = RandomStringUtils.generateString(12);
		System.out.println(s2);
		String s3 = RandomStringUtils.generateLetterString(12);
		System.out.println(s3);
		String s4 = RandomStringUtils.generateLowerString(12);
		System.out.println(s4);
		String s5 = RandomStringUtils.generateUpperString(12);
		System.out.println(s5);
		String s6 = RandomStringUtils.generateFromIntArray(new int[] { 1, 2, 3, 4, 5 }, 4);
		System.out.println(s6);

	}

}
