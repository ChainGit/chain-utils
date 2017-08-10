package com.chain.test;

import org.junit.Test;

import com.chain.utils.PrintUtils;
import com.chain.utils.RandomIntegerArrayUtils;

public class RandomIntegerArrayUtilsTest {

	@Test
	public void test() {
		int a[] = RandomIntegerArrayUtils.randomDoubleCirculation(0, 9, 10);
		PrintUtils.show(a);

		int b[] = RandomIntegerArrayUtils.randomSelectArray(0, 9, 10);
		PrintUtils.show(b);

		int c[] = RandomIntegerArrayUtils.randomHashSet(0, 9, 10);
		PrintUtils.show(c);

		int d[] = RandomIntegerArrayUtils.randomByList(0, 9, 10);
		PrintUtils.show(d);

		int e[] = RandomIntegerArrayUtils.randomSelectArray(-6, 9, 5);
		PrintUtils.show(e);

		int f[] = RandomIntegerArrayUtils.randomDoubleCirculation(-6, 9, 7);
		PrintUtils.show(f);

		// other test
		// int g[] = randomDoubleCirculation(-6, 9, 20);
		// TestMethod.print(g);
	}
}
