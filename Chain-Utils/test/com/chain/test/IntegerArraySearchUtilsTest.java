package com.chain.test;

import org.junit.Test;

import com.chain.utils.IntegerArraySearchUtils;
import com.chain.utils.IntegerArraySortUtils;
import com.chain.utils.PrintUtils;
import com.chain.utils.RandomIntegerArrayUtils;

public class IntegerArraySearchUtilsTest {

	@Test
	public void test() {
		int[] a = RandomIntegerArrayUtils.randomSelectArray(0, 9, 10);
		PrintUtils.show(a);

		System.out.println(IntegerArraySearchUtils.getMax(a));
		System.out.println(IntegerArraySearchUtils.getMin(a));

		System.out.println(IntegerArraySearchUtils.orderSearch(a, 4));
		System.out.println(IntegerArraySearchUtils.orderSearch(a, 12));

		IntegerArraySortUtils.quickSort(a);
		PrintUtils.show(a);
		System.out.println(IntegerArraySearchUtils.halfSearch(a, 8));
		System.out.println(IntegerArraySearchUtils.halfSearch(a, 10));
		System.out.println(IntegerArraySearchUtils.halfSearch(a, 20));

		a = new int[] { 2, 4, 7, 9, 11, 15, 23, 27, 29, 31, 35, 40, 43, 50, 65 };
		System.out.println(IntegerArraySearchUtils.halfSearch(a, 43));

		a = RandomIntegerArrayUtils.randomSelectArray(0, 1000, 500);
		System.out.println(IntegerArraySearchUtils.orderSearch(a, 100));
		System.out.println(IntegerArraySearchUtils.hashSearch(a, 100));

		// other test
		// System.out.println(IntegerArraySearchUtils.halfSearch(null, 8));
	}

}
