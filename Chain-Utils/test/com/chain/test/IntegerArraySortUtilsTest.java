package com.chain.test;

import org.junit.Test;

import com.chain.utils.IntegerArraySortUtils;
import com.chain.utils.PrintUtils;
import com.chain.utils.RandomIntegerArrayUtils;

public class IntegerArraySortUtilsTest {

	@Test
	public void test() {
		int a[] = RandomIntegerArrayUtils.randomSelectArray(0, 9, 10);
		PrintUtils.show(a);

		int b[] = a.clone();
		IntegerArraySortUtils.swap(b, 3, 6);
		PrintUtils.show(b);
		// IntegerArraySortUtils.swap2(b, 1, 4);
		// PrintUtils.show(b);
		// IntegerArraySortUtils.swap3(b, 5, 2);
		// PrintUtils.show(b);

		int c1[] = a.clone();
		IntegerArraySortUtils.insertSort(c1);
		PrintUtils.show(c1);

		int c2[] = a.clone();
		IntegerArraySortUtils.bubbleSort(c2);
		PrintUtils.show(c2);

		int c3[] = a.clone();
		IntegerArraySortUtils.selectSort(c3);
		PrintUtils.show(c3);

		int c4[] = a.clone();
		IntegerArraySortUtils.shellSort(c4);
		PrintUtils.show(c4);

		int c5[] = a.clone();
		IntegerArraySortUtils.quickSort(c5);
		PrintUtils.show(c5);

		// other test
		int d[] = { 2, 4, 5, 6, 2, 0, 2 };
		int d1[] = d.clone();
		IntegerArraySortUtils.insertSort(d1);
		PrintUtils.show(d1);

		int d2[] = d.clone();
		IntegerArraySortUtils.bubbleSort(d2);
		PrintUtils.show(d2);

		int d3[] = d.clone();
		IntegerArraySortUtils.selectSort(d3);
		PrintUtils.show(d3);

		int d4[] = d.clone();
		IntegerArraySortUtils.shellSort(d4);
		PrintUtils.show(d4);

		int d5[] = d.clone();
		IntegerArraySortUtils.quickSort(d5);
		PrintUtils.show(d5);

		// insertSort(null);
	}

}
