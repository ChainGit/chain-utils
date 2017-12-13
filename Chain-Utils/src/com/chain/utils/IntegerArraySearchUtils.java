package com.chain.utils;

import com.chain.exception.ChainUtilsRuntimeException;

/**
 * 
 * 常见的查找方法，包括<i>顺序查找，折半查找，最值查找</i>
 * 
 * @author Chain Qiao
 * @version 1.0
 *
 */
public class IntegerArraySearchUtils {

	/**
	 * 找出整型数组中的<i>最小值</i>
	 * 
	 * @param a
	 *            要查找的数组
	 * @return 返回数组中最小元素的值
	 */
	public static int getMin(int[] a) {
		checkArray(a);
		int p = 0;
		for (int i = 0; i < a.length; i++)
			if (a[p] > a[i])
				p = i;
		return a[p];
	}

	/**
	 * 找出整型数组中的<i>最大值</i>
	 * 
	 * @param a
	 *            要查找的数组
	 * @return 返回数组中最大元素的值
	 */
	public static int getMax(int[] a) {
		checkArray(a);
		int p = 0;
		for (int i = 0; i < a.length; i++)
			if (a[p] < a[i])
				p = i;
		return a[p];
	}

	private static void checkArray(int[] a) {
		if (a == null || a.length < 1)
			throw new ChainUtilsRuntimeException("array is null or empty");
	}

	/**
	 * 线性查找
	 * 
	 * @param a
	 *            待查找数组
	 * @param k
	 *            查找的数据
	 * @return 若找到则返回元素所在数组的<i>下标</i>，若没有找到则返回-1
	 */
	public static int orderSearch(int[] a, int k) {
		checkArray(a);
		int index = -1;
		for (index = 0; index < a.length; index++) {
			if (a[index] == k)
				return index;
		}
		return -1;
	}

	/**
	 * 折半查找(二分查找)，<b>必须有序数组或者已知固定数组</b>
	 * 
	 * @param a
	 *            待查找数组
	 * @param k
	 *            查找的数据
	 * @return 若找到则返回元素所在数组的<i>下标</i>，若没有找到则返回查找的元素在序列<i>可插入的位置的负数</i>
	 */
	public static int halfSearch(int[] a, int k) {
		checkArray(a);
		int min, max, mid;
		min = 0;
		max = a.length - 1;

		do {
			mid = (max + min) >>> 1;

			if (k < a[mid])
				max = mid - 1;
			else if (k > a[mid])
				min = mid + 1;
			else if (k == a[mid])
				return mid;

		} while (min <= max);
		return -1 - min;
	}

	/**
	 * 利用hash查找无序表中的某一个元素（只是演示算法，并不是最优的）
	 * 
	 * @param a
	 *            待查找数组
	 * @param k
	 *            查找的数据
	 * @return 若找到则返回元素所在数组的<i>下标</i>，若没有找到返回-1
	 */
	public static int hashSearch(int[] a, int k) {
		int base = 10;
		int len = a.length;
		if (len < base)
			return orderSearch(a, k);
		int[] f = new int[base];
		int[][] h = new int[base][len];
		int[][] s = new int[base][len];
		for (int i = 0; i < len; i++) {
			int v = Math.abs(a[i]) % base;
			h[v][f[v]] = a[i];
			s[v][f[v]++] = i;
		}
		int e = Math.abs(k) % base;
		int p = orderSearch(h[e], k);
		return p == -1 ? -1 : s[e][p];
	}
}
