package com.chain.utils;

import com.chain.exception.ChainUtilsRuntimeException;

/**
 * 
 * 一些常见的排序算法，包括<i>插入排序，选择排序，冒泡排序，希尔排序，快速排序，交换元素</i>
 * 
 * @author Chain Qian
 * @version 1.0
 *
 */
public class IntegerArraySortUtils {

	/**
	 * 异或方式交换元素，没有中间变量，不会增加数值长度
	 * 
	 * @param n
	 *            要交换元素的数组
	 * @param i
	 *            第一个交换元素的数组下标
	 * @param j
	 *            第二个交换元素的数组小标
	 */
	public static void swap(int[] n, int i, int j) {
		checkSwapArgs(n, i, j);
		n[i] ^= n[j];
		n[j] ^= n[i];
		n[i] ^= n[j];
	}

	/**
	 * 
	 * 加减方式交换元素，注意可能带来的数据溢出和符号位问题
	 * 
	 * @param n
	 *            要交换元素的数组
	 * @param i
	 *            第一个交换元素的数组下标
	 * @param j
	 *            第二个交换元素的数组小标
	 */
	protected static void swap2(int[] n, int i, int j) {
		checkSwapArgs(n, i, j);
		n[i] = n[i] + n[j];
		n[j] = n[i] - n[j];
		n[i] = n[i] - n[j];
	}

	/**
	 * 
	 * 中间变量法，方法简单，带来额外的内存开销，不过开销很小
	 * 
	 * @param n
	 *            要交换元素的数组
	 * @param i
	 *            第一个交换元素的数组下标
	 * @param j
	 *            第二个交换元素的数组小标
	 */
	protected static void swap3(int[] n, int i, int j) {
		checkSwapArgs(n, i, j);
		int t = n[i];
		n[i] = n[j];
		n[j] = t;
	}

	private static void checkSwapArgs(int[] n, int i, int j) {
		if (i < 0 || j > n.length - 1)
			throw new ChainUtilsRuntimeException("index is illegal");
	}

	private static void checkArray(int[] a) {
		if (a == null || a.length < 1)
			throw new ChainUtilsRuntimeException("array is null or empty");
	}

	/**
	 * 冒泡排序：相邻比较，每次将尚未排序的序列中最大的元素移动到末尾
	 * 
	 * @param a
	 *            要排序的数组
	 */
	public static void bubbleSort(int[] a) {
		// TODO Auto-generated method stub
		checkArray(a);
		int n = a.length;
		boolean flag = false;// false代表没有排好序
		for (int i = 0; i < n - 1 && !flag; i++) {
			flag = true;
			for (int j = 0; j < n - 1 - i; j++) {
				if (a[j] > a[j + 1]) {
					flag = false;
					swap(a, j, j + 1);
				}
			}
		}
	}

	/**
	 * 选择排序：每次从剩余尚未排序的序列中选择一个最小的接在已排序的部分后面
	 * 
	 * @param a
	 *            要排序的数组
	 */
	public static void selectSort(int[] a) {
		// TODO Auto-generated method stub
		checkArray(a);
		int n = a.length;
		for (int i = 0; i < n - 1; i++) {
			int min = i;
			for (int j = i + 1; j < n; j++)
				if (a[j] < a[min])
					min = j;
			if (min != i)
				swap(a, i, min);
		}
	}

	/**
	 * 插入排序：每次取尚未排序的序列的第一个元素插入到前面序列的合适位置
	 * 
	 * @param a
	 *            要排序的数组
	 */
	public static void insertSort(int[] a) {
		// TODO Auto-generated method stub
		checkArray(a);
		int n = a.length;
		for (int i = 1; i < n; i++) {
			// 方法一：类似降序的冒泡排序法
			// method3a(a, i);

			// 方法二：中间变量
			// method3b(a, i);

			// 方法三：中间变量2
			method3c(a, i);

		}
	}

	private static void method3c(int[] a, int i) {
		int t = a[i];
		int j = i - 1;
		for (; j > -1 && a[j] > t; j--)
			a[j + 1] = a[j];
		a[j + 1] = t;
	}

	@SuppressWarnings("unused")
	private static void method3b(int[] a, int i) {
		int t = a[i];
		int j = i;
		for (; j > 0 && a[j] > t; j--)
			a[j] = a[j - 1];
		a[j] = t;
	}

	@SuppressWarnings("unused")
	private static void method3a(int[] a, int i) {
		for (int j = i - 1; j > -1; j--)
			if (a[i] < a[j])
				swap(a, i, j);
	}

	/**
	 * 希尔排序：设定一个gap，从序列开头开始，每隔一个gap取一个数，然后对取出的数排序(按常规排序法)。每次 循环后gap减小一半，最终细化。
	 * 
	 * @param a
	 *            要排序的数组
	 */
	public static void shellSort(int[] a) {
		// TODO Auto-generated method stub
		checkArray(a);
		int n = a.length;
		for (int gap = n; gap > 0; gap >>= 1) {
			// 冒泡排序法
			// method4a(n, a, gap);

			// 选择排序法
			// method4b(n, a, gap);

			// 插入排序
			method4c(n, a, gap);

		}
	}

	private static void method4c(int n, int[] a, int gap) {
		for (int i = gap; i < n; i += gap) {
			int t = a[i];
			int j = i - gap;
			for (; j > -gap && a[j] > t; j -= gap)
				a[j + gap] = a[j];
			a[j + gap] = t;
		}
	}

	@SuppressWarnings("unused")
	private static void method4b(int n, int[] a, int gap) {
		for (int i = gap; i < n - gap; i += gap) {
			int min = i;
			for (int j = i + gap; j < n; j += gap)
				if (a[j] < a[min])
					min = j;
			if (min != i)
				swap(a, i, min);
		}
	}

	@SuppressWarnings("unused")
	private static void method4a(int n, int[] a, int gap) {
		boolean flag = false;
		for (int i = 0; i < n - gap && !flag; i += gap) {
			flag = true;
			for (int j = 0; j < n - gap - i; j += gap) {
				if (a[j] > a[j + gap])
					swap(a, j, j + gap);
				flag = false;
			}
		}
	}

	/**
	 * 快速排序：选择一个数作为基准(一般是第一个数),然后将之后的序列中比这个基准数小的数放在左边，比它大的数放在右边。接着将基准数放在“中间”，通过递归分别再处理左边和右边。
	 * 
	 * @param a
	 *            要排序的数组
	 */
	public static void quickSort(int[] a) {
		// TODO Auto-generated method stub
		checkArray(a);
		int n = a.length;
		int le = 0;
		int ri = n - 1;
		quickFun(a, le, ri);
	}

	private static void quickFun(int[] a, int le, int ri) {

		if (le < 0 || ri > a.length - 1 || le >= ri)
			return;

		int i = le;
		int j = ri;
		int base = a[le];
		while (i < j) {
			while (a[j] > base && i < j)
				j--;
			if (i < j) {
				a[i] = a[j];
				i++;
			}
			while (a[i] < base && i < j)
				i++;
			if (i < j) {
				a[j] = a[i];
				j--;
			}
		}
		a[i] = base;

		quickFun(a, le, i - 1);
		quickFun(a, i + 1, ri);

	}

}
