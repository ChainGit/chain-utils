package com.chain.utils;

import org.slf4j.Logger;

import com.chain.exception.ChainUtilsRuntimeException;
import com.chain.logging.ChainUtilsLoggerFactory;

/**
 * 整数的位运算工具类（很强大的）
 * 
 * http://www.cnblogs.com/zichi/p/4789439.html
 * 
 * @author Chain Qian
 * @version 1.0
 * 
 */
public class BitUtils {

	private static final Logger logger = ChainUtilsLoggerFactory.getLogger(BitUtils.class);

	private static final int INT_SIZE = 32;

	// ----------------- 从右往左的操作，支持负数情况（右边第k位系列）-----------------

	/**
	 * 取从右往左数第k位
	 * 
	 * @param n
	 *            被操作的数
	 * @param k
	 *            从右往左数第k位
	 * @return 结果
	 */
	public static int getRightK(int n, int k) {
		return (n >> (k - 1)) & 1;
	}

	/**
	 * 从右往左数第k位 置0
	 * 
	 * @param n
	 *            被操作的数
	 * @param k
	 *            从右往左数第k位
	 * @return 结果
	 */
	public static int setRightKToZero(int n, int k) {
		return n & ~(1 << (k - 1));
	}

	/**
	 * 从右往左数第k位 置1
	 * 
	 * @param n
	 *            被操作的数
	 * @param k
	 *            从右往左数第k位
	 * @return 结果
	 */
	public static int setRightKToOne(int n, int k) {
		return n | (1 << (k - 1));
	}

	/**
	 * 从右往左数第k位 取反
	 * 
	 * @param n
	 *            被操作的数
	 * @param k
	 *            从右往左数第k位
	 * @return 结果
	 */
	public static int inverseRightK(int n, int k) {
		return n ^ (1 << (k - 1));
	}

	// ----------------- 从左往右的操作比较麻烦，注意负数不受支持 （右边第k位系列）-----------------

	// 获得二进制下，从第一位开始数，第一次出现非0的位置，负数始终返回1，所以是不支持的
	private static int getStartKNotZero(int n) {
		checkN(n);

		int t = 0;
		int i = 0;
		for (; i < INT_SIZE; i++) {
			t = getRightK(n, INT_SIZE - i);
			if (t != 0)
				break;
		}
		return i + 1;
	}

	/**
	 * 取从左往右数第k位
	 * 
	 * @param n
	 *            被操作的数
	 * @param k
	 *            从左往右数第k位
	 * @return 结果
	 */
	public static int getLeftK(int n, int k) {
		return getRightK(n, INT_SIZE - getStartKNotZero(n) - k + 2);
	}

	/**
	 * 从左往右数第k位 置0
	 * 
	 * @param n
	 *            被操作的数
	 * @param k
	 *            从左往右数第k位
	 * @return 结果
	 */
	public static int setLeftKToZero(int n, int k) {
		return setRightKToZero(n, INT_SIZE - getStartKNotZero(n) - k + 2);
	}

	/**
	 * 从左往右数第k位 置1
	 * 
	 * @param n
	 *            被操作的数
	 * @param k
	 *            从左往右数第k位
	 * @return 结果
	 */
	public static int setLeftKToOne(int n, int k) {
		return setRightKToOne(n, INT_SIZE - getStartKNotZero(n) - k + 2);
	}

	/**
	 * 从左往右数第k位 取反
	 * 
	 * @param n
	 *            被操作的数
	 * @param k
	 *            从左往右数第k位
	 * @return 结果
	 */
	public static int inverseLeftK(int n, int k) {
		return inverseRightK(n, INT_SIZE - getStartKNotZero(n) - k + 2);
	}

	// ----------------- 从右往左区间操作，支持负数的情况（末k位系列） -----------------

	/**
	 * 获得从右往左数到第k位的这一段
	 * 
	 * @param n
	 *            被操作的数
	 * @param k
	 *            从左往右数第k位
	 * @return 结果
	 */
	public static int getRightToK(int n, int k) {
		return n & ((1 << k) - 1);
	}

	/**
	 * 把从右往左数到第k位的这一段都置为0
	 * 
	 * @param n
	 *            被操作的数
	 * @param k
	 *            从左往右数第k位
	 * @return 结果
	 */
	public static int setRightToKAllToZero(int n, int k) {
		return n & ~((1 << k) - 1);
	}

	/**
	 * 把从右往左数到第k位的这一段都置为1
	 * 
	 * @param n
	 *            被操作的数
	 * @param k
	 *            从左往右数第k位
	 * @return 结果
	 */
	public static int setRightToKAllToOne(int n, int k) {
		return n | ((1 << k) - 1);
	}

	/**
	 * 把从右往左数到第k位的这一段取反
	 * 
	 * @param n
	 *            被操作的数
	 * @param k
	 *            从左往右数第k位
	 * @return 结果
	 */
	public static int inverseRightToK(int n, int k) {
		return n ^ ((1 << k) - 1);
	}

	// ----------------- 右边连续0或者1系列 -----------------

	/**
	 * 把右边连续的1（右边第一位需要为1）变为0
	 * 
	 * @param n
	 *            被操作的数
	 * @return 结果
	 */
	public static int setRightContinuousOneToZero(int n) {
		return n & (n + 1);
	}

	/**
	 * 取右边连续的1（右边第一位需要为1）
	 * 
	 * @param n
	 *            被操作的数
	 * @return 结果
	 */
	public static int getRightContinuousOne(int n) {
		return (n ^ (n + 1)) >> 1;
	}

	/**
	 * 把右边连续的0（右边第一位需要为0）变为1
	 * 
	 * @param n
	 *            被操作的数
	 * @return 结果
	 */
	public static int setRightContinuousZeroToOne(int n) {
		return n | (n - 1);
	}

	/**
	 * 去除右边连续的0（右边第一位需要为0）
	 * 
	 * @param n
	 *            被操作的数
	 * @return 结果
	 */
	public static int removeRightContinuousZero(int n) {
		return n >> (int) (Math.log(n & -n) / Math.log(2));
	}

	// ----------------- 右边第一个0或者1系列 -----------------

	/**
	 * 将从右往左看出现的第一个1改为0
	 * 
	 * @param n
	 *            被操作的数
	 * @return 结果
	 */
	public static int setRightFirstOneToZero(int n) {
		return n & (n - 1);
	}

	/**
	 * 将从右往左看出现的第一个0改为1
	 * 
	 * @param n
	 *            被操作的数
	 * @return 结果
	 */
	public static int setRightFirstZeroToOne(int n) {
		return n | (n + 1);
	}

	/**
	 * 获得从右往左看出现的第一个为1的位的下标对应的十进制数（下标从1开始，也是从右往左数）
	 * 
	 * 如果返回0，则代表没有找到
	 * 
	 * @param n
	 *            被操作的数
	 * @return 结果
	 */
	public static int getRightFirstOneIndexOfDecimal(int n) {
		return n & -n;
	}

	/**
	 * 获得从右往左看出现的第一个为1的位的下标（下标从1开始，也是从右往左数）
	 * 
	 * 如果返回0，则代表没有找到
	 * 
	 * @param n
	 *            被操作的数
	 * @return 结果
	 */
	public static int getRightFirstOneIndex(int n) {
		int p = getRightFirstOneIndexOfDecimal(n);
		if (p == 0)
			return p;
		int i = 1;
		while (p != 1) {
			p >>>= 1;
			i++;
		}
		return i;
	}

	// ----------------- 其他操作 -----------------

	/**
	 * 乘2的k次幂
	 * 
	 * 
	 * @param n
	 *            被操作的数
	 * @param k
	 *            k次幂
	 * @return 结果
	 */
	public static int getMulPowerK(int n, int k) {
		return n << k;
	}

	/**
	 * 除2的k次幂
	 * 
	 * 
	 * @param n
	 *            被操作的数
	 * @param k
	 *            k次幂
	 * @return 结果
	 */
	public static int getDivPowerK(int n, int k) {
		checkN(n);
		return n >>> k;
	}

	/**
	 * 是否是偶数
	 * 
	 * @param n
	 *            被操作的数
	 * @return 结果
	 */
	public static boolean isEven(int n) {
		checkN(n);
		return (n & 1) == 0;
	}

	/**
	 * 是否是奇数
	 * 
	 * @param n
	 *            被操作的数
	 * @return 结果
	 */
	public static boolean isOdd(int n) {
		checkN(n);
		return !isEven(n);
	}

	/**
	 * 交换数组的第i和j元素的位置
	 * 
	 * @param a
	 *            被处理的数组
	 * @param i
	 *            位置
	 * @param j
	 *            位置
	 */
	public static void swap(int[] a, int i, int j) {
		int m = a[i - 1];
		int n = a[j - 1];
		m = m ^ n;
		n = m ^ n;
		m = m ^ n;
		a[i - 1] = m;
		a[j - 1] = n;
	}

	/**
	 * 取相反数
	 * 
	 * @param n
	 *            被操作的数
	 * @return 结果
	 */
	public static int opposite(int n) {
		return (n ^ -1) + 1;
		// return ~n + 1;
	}

	/**
	 * 模2的k次幂
	 * 
	 * 
	 * @param n
	 *            被操作的数
	 * @param k
	 *            k次幂
	 * @return 结果
	 */
	public static int getModPowerK(int n, int k) {
		checkN(n);
		return n & ((1 << k) - 1);
		// return n % (1 << 4);
	}

	/**
	 * 判断是否是2的幂
	 * 
	 * @param n
	 *            被操作的数
	 * @return 结果
	 */
	public static boolean isPowerOfTwo(int n) {
		checkN(n);
		return (n & (n - 1)) == 0;
	}

	/**
	 * 取绝对值
	 * 
	 * @param n
	 *            被操作的数
	 * @return 结果
	 */
	public static int abs(int n) {
		return (n ^ (n >> 31)) - (n >> 31);
		// return n ^ ( ~(n >> 31) + 1) + (n >> 31);
	}

	/**
	 * 判断两个数的符号是否相同
	 * 
	 * @param a
	 *            被判断的数a
	 * @param b
	 *            被判断的数b
	 * @return 结果
	 */
	public static boolean isSameSignal(int a, int b) {
		return (a ^ b) > 0;
	}

	// 在某些情况下，负数不受支持
	private static void checkN(int n) {
		if (n < 0)
			throw new ChainUtilsRuntimeException("not support negative int number");
	}
}
