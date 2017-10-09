package com.chain.test;

import org.junit.Test;

import com.chain.utils.IntegerArraySortUtils;
import com.chain.utils.PrintUtils;
import com.chain.utils.RandomIntegerArrayUtils;

public class IntegerArraySortUtilsTest {

	@Test
	public void test() {
		// 非重复元素排序
		int a[] = RandomIntegerArrayUtils.randomSelectArray(0, 100, 20);
		PrintUtils.show(a);

		System.out.println();

		int b1[] = a.clone();
		IntegerArraySortUtils.swap(b1, 3, 6);
		PrintUtils.show(b1);

		// int b2[] = a.clone();
		// IntegerArraySortUtils.swap2(b2, 1, 4);
		// PrintUtils.show(b2);

		// int b3[] = a.clone();
		// IntegerArraySortUtils.swap3(b3, 5, 2);
		// PrintUtils.show(b3);

		System.out.println();

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

		int c6[] = a.clone();
		IntegerArraySortUtils.cockTailSort(c6);
		PrintUtils.show(c6);

		int c7[] = a.clone();
		IntegerArraySortUtils.binarySearchInsertSort(c7);
		PrintUtils.show(c7);

		int c8[] = a.clone();
		IntegerArraySortUtils.countSort(c8);
		PrintUtils.show(c8);

		int c9[] = a.clone();
		IntegerArraySortUtils.mergeSort(c9);
		PrintUtils.show(c9);

		int c10[] = a.clone();
		IntegerArraySortUtils.buttomUpMergeSort(c10);
		PrintUtils.show(c10);

		int c11[] = a.clone();
		IntegerArraySortUtils.radixSort(c11);
		PrintUtils.show(c11);

		int c12[] = a.clone();
		IntegerArraySortUtils.bucketSort(c12);
		PrintUtils.show(c12);

		int c13[] = a.clone();
		IntegerArraySortUtils.heapSort(c13);
		PrintUtils.show(c13);

		System.out.println();

		// 重复元素排序
		int d[] = { 2, 4, 5, 6, 2, 0, 2, 6, 9, 0, 4, 5, 6, 9, 2, 6 };
		PrintUtils.show(d);

		System.out.println();

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

		int d6[] = d.clone();
		IntegerArraySortUtils.cockTailSort(d6);
		PrintUtils.show(d6);

		int d7[] = d.clone();
		IntegerArraySortUtils.binarySearchInsertSort(d7);
		PrintUtils.show(d7);

		int d8[] = d.clone();
		IntegerArraySortUtils.countSort(d8);
		PrintUtils.show(d8);

		int d9[] = d.clone();
		IntegerArraySortUtils.mergeSort(d9);
		PrintUtils.show(d9);

		int d10[] = d.clone();
		IntegerArraySortUtils.buttomUpMergeSort(d10);
		PrintUtils.show(d10);

		int d11[] = d.clone();
		IntegerArraySortUtils.radixSort(d11);
		PrintUtils.show(d11);

		int d12[] = d.clone();
		IntegerArraySortUtils.bucketSort(d12);
		PrintUtils.show(d12);

		int d13[] = d.clone();
		IntegerArraySortUtils.heapSort(d13);
		PrintUtils.show(d13);

	}

}
