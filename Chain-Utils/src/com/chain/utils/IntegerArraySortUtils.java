package com.chain.utils;

import com.chain.exception.ChainUtilsRuntimeException;

/**
 * 
 * 一些常见的排序算法，包括<i>插入排序，二分插入排序，选择排序，冒泡排序，鸡尾酒排序，希尔排序，快速排序，归并排序，桶排序，基数排序，计数排序，堆排序，和交换元素</i>
 * 
 * @author Chain Qian
 * @version 1.1
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
	// 分类 -------------- 内部比较排序
	// 数据结构 ---------- 数组
	// 最差时间复杂度 ---- O(n^2)
	// 最优时间复杂度 ---- 如果能在内部循环第一次运行时,使用一个旗标来表示有无需要交换的可能,可以把最优时间复杂度降低到O(n)
	// 平均时间复杂度 ---- O(n^2)
	// 所需辅助空间 ------ O(1)
	// 稳定性 ------------ 稳定
	public static void bubbleSort(int[] a) {
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
	 * 定向冒泡排序，或鸡尾酒排序：从低到高然后又从高到低
	 * 
	 * @param a
	 *            要排序的数组
	 */
	// 分类 -------------- 内部比较排序
	// 数据结构 ---------- 数组
	// 最差时间复杂度 ---- O(n^2)
	// 最优时间复杂度 ---- 如果序列在一开始已经大部分排序过的话,会接近O(n)
	// 平均时间复杂度 ---- O(n^2)
	// 所需辅助空间 ------ O(1)
	// 稳定性 ------------ 稳定
	public static void cockTailSort(int[] a) {
		checkArray(a);
		int n = a.length;
		boolean flag = false;// false代表没有排好序
		int left = 0;
		int right = n - 1;
		while (left < right && !flag) {
			// 先从左到右，升序
			for (int i = left; i < right && !flag; i++) {
				if (a[i] > a[i + 1]) {
					flag = false;
					swap(a, i, i + 1);
				}
			}
			if (flag)
				break;
			right--;
			// 在从右到左，降序
			for (int i = right; i > left && !flag; i--) {
				if (a[i - 1] > a[i]) {
					flag = false;
					swap(a, i - 1, i);
				}
			}
			if (flag)
				break;
			left++;
		}
	}

	/**
	 * 选择排序：每次从剩余尚未排序的序列中选择一个最小的接在已排序的部分后面
	 * 
	 * @param a
	 *            要排序的数组
	 */
	// 分类 -------------- 内部比较排序
	// 数据结构 ---------- 数组
	// 最差时间复杂度 ---- O(n^2)
	// 最优时间复杂度 ---- O(n^2)
	// 平均时间复杂度 ---- O(n^2)
	// 所需辅助空间 ------ O(1)
	// 稳定性 ------------ 不稳定
	public static void selectSort(int[] a) {
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
	// 分类 ------------- 内部比较排序
	// 数据结构 ---------- 数组
	// 最差时间复杂度 ---- 最坏情况为输入序列是降序排列的,此时时间复杂度O(n^2)
	// 最优时间复杂度 ---- 最好情况为输入序列是升序排列的,此时时间复杂度O(n)
	// 平均时间复杂度 ---- O(n^2)
	// 所需辅助空间 ------ O(1)
	// 稳定性 ------------ 稳定
	public static void insertSort(int[] a) {
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
	 * 类二分查找的插入排序，适合元素数量较多的情况。
	 * 
	 * @param a
	 *            要排序的数组
	 */
	// 分类 -------------- 内部比较排序
	// 数据结构 ---------- 数组
	// 最差时间复杂度 ---- O(n^2)
	// 最优时间复杂度 ---- O(nlogn)
	// 平均时间复杂度 ---- O(n^2)
	// 所需辅助空间 ------ O(1)
	// 稳定性 ------------ 稳定
	public static void binarySearchInsertSort(int[] a) {
		checkArray(a);
		int n = a.length;
		for (int i = 1; i < n; i++) {
			int t = a[i];
			int left = 0;
			int right = i - 1;
			while (left <= right) {
				int mid = (right - left) / 2 + left;
				if (a[mid] > t)
					right = mid - 1;
				else
					left = mid + 1;
			}
			for (int j = i - 1; j >= left; j--) {
				a[j + 1] = a[j];
			}
			a[left] = t;
		}
	}

	/**
	 * 计数排序：适用于数值范围较小的数组，比如字符串排序，0-9排序等。
	 * 
	 * 非比较排序。
	 * 
	 * @param a
	 *            要排序的数组
	 */
	// 分类 ------------ 内部非比较排序
	// 数据结构 --------- 数组
	// 最差时间复杂度 ---- O(n + k)
	// 最优时间复杂度 ---- O(n + k)
	// 平均时间复杂度 ---- O(n + k)
	// 所需辅助空间 ------ O(n + k)
	// 稳定性 ----------- 稳定
	public static void countSort(int[] a) {
		checkArray(a);
		int n = a.length;
		int[] r = new int[n];
		int max = a[0];
		int min = max;
		for (int i : a) {
			if (i > max)
				max = i;
			if (i < min)
				min = i;
		}
		// 用于统计
		int len = max - min + 1;
		int[] f = new int[len];
		// 统计出现的次数
		for (int i = 0; i < n; i++)
			f[a[i] - min]++;
		// 计算相对位移
		for (int i = 1; i < len; i++)
			f[i] += f[i - 1];
		// 倒序输出数组（稳定）
		for (int i = n - 1; i > -1; i--)
			r[--f[a[i] - min]] = a[i];
		// 再拷贝回去
		for (int i = 0; i < n; i++)
			a[i] = r[i];
	}

	/**
	 * 希尔排序：设定一个gap，从序列开头开始，每隔一个gap取一个数，然后对取出的数排序(按常规排序法)。每次 循环后gap减小一半，最终细化。
	 * 
	 * 内部使用类似插入排序
	 * 
	 * @param a
	 *            要排序的数组
	 */
	// 分类 -------------- 内部比较排序
	// 数据结构 ---------- 数组
	// 最差时间复杂度 ---- 根据步长序列的不同而不同。已知最好的为O(n(logn)^2)
	// 最优时间复杂度 ---- O(n)
	// 平均时间复杂度 ---- 根据步长序列的不同而不同。
	// 所需辅助空间 ------ O(1)
	// 稳定性 ------------ 不稳定
	public static void shellSort(int[] a) {
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
	// 分类 ------------ 内部比较排序
	// 数据结构 --------- 数组
	// 最差时间复杂度 ---- 每次选取的基准都是最大（或最小）的元素，导致每次只划分出了一个分区，需要进行n-1次划分才能结束递归，时间复杂度为O(n^2)
	// 最优时间复杂度 ---- 每次选取的基准都是中位数，这样每次都均匀的划分出两个分区，只需要logn次划分就能结束递归，时间复杂度为O(nlogn)
	// 平均时间复杂度 ---- O(nlogn)
	// 所需辅助空间 ------
	// 主要是递归造成的栈空间的使用(用来保存left和right等局部变量)，取决于递归树的深度，一般为O(logn)，最差为O(n)
	// 稳定性 ---------- 不稳定
	public static void quickSort(int[] a) {
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

	/**
	 * 归并排序：自顶而下，递归做法
	 * 
	 * @param a
	 *            要排序的数组
	 */
	// 分类 -------------- 内部比较排序
	// 数据结构 ---------- 数组
	// 最差时间复杂度 ---- O(nlogn)
	// 最优时间复杂度 ---- O(nlogn)
	// 平均时间复杂度 ---- O(nlogn)
	// 所需辅助空间 ------ O(n)
	// 稳定性 ------------ 稳定
	public static void mergeSort(int[] a) {
		checkArray(a);
		int n = a.length;
		int low = 0;
		int high = n - 1;
		mergeSortPartSort(a, low, high);
	}

	private static void mergeSortPartMerge(int[] a, int low, int mid, int high) {
		// 两个指针
		int p = low;
		int q = mid + 1;
		int len = high - low + 1;
		// 临时数组，用于排序的备份
		int[] t = new int[len];
		for (int i = low; i <= high; i++)
			t[i - low] = a[i];
		for (int i = low; i <= high; i++) {
			// 左半边已无剩余
			if (p > mid) {
				a[i] = t[q++ - low];
			}
			// 右半边已无剩余
			else if (q > high) {
				a[i] = t[p++ - low];
			}
			// 右半边当前元素小于左半边当前元素，取右半边的元素
			else if (t[q - low] < t[p - low]) {
				a[i] = t[q++ - low];
			}
			// 右半边当前元素大于或等于左半边当前元素，取左半边的元素
			else {
				a[i] = t[p++ - low];
			}
		}
	}

	// 自顶而下，递归做法
	private static void mergeSortPartSort(int[] a, int low, int high) {
		// 分到每组一个元素为止
		if (low >= high)
			return;

		int mid = (high - low) / 2 + low;
		// 左右两边分治，分别进行排序
		mergeSortPartSort(a, low, mid);
		mergeSortPartSort(a, mid + 1, high);
		// 左右两边归并
		mergeSortPartMerge(a, low, mid, high);
	}

	/**
	 * 归并排序：自底而上，循环做法
	 * 
	 * @param a
	 *            要排序的数组
	 */
	public static void buttomUpMergeSort(int[] a) {
		checkArray(a);
		int n = a.length;
		int high = n - 1;
		// 进行logN次两两归并
		// p代表size
		int delta = 0;
		for (int p = 1; p <= high; p = delta) {
			delta = p << 1;
			// 每个size下对每一个划分的小组进行归并
			for (int low = 0; low <= high - p; low += delta) {
				mergeSortPartMerge(a, low, low + p - 1, Math.min(low + delta - 1, high));
			}
		}
	}

	/**
	 * 基数排序：非比较排序算法，这里是最低位优先基数排序LSD(Least significant digit)
	 * 
	 * @param a
	 *            要排序的数组
	 */
	// 分类 ------------- 内部非比较排序
	// 数据结构 ---------- 数组
	// 最差时间复杂度 ---- O(n * dn)
	// 最优时间复杂度 ---- O(n * dn)
	// 平均时间复杂度 ---- O(n * dn)
	// 所需辅助空间 ------ O(n * dn)
	// 稳定性 ----------- 稳定
	public static void radixSort(int[] a) {
		checkArray(a);
		radixSort(a, DECIMAL);
	}

	private static final int DECIMAL = 10;

	private static void radixSort(int[] a, int radix) {
		int max = a[0];
		int min = max;
		for (int i : a) {
			if (i > max)
				max = i;
			if (i < min)
				min = i;
		}
		// 最大和最小的差
		int delta = max - min;
		// 指数
		int exponent = 1;
		// 从低位开始往高位依次进行按位计数排序
		while (delta / exponent >= 1) {
			countSortByDigit(a, radix, exponent, delta, min);
			exponent *= radix;
		}
	}

	private static void countSortByDigit(int[] a, int radix, int exponent, int delta, int min) {
		int n = a.length;
		// 用于统计
		int len = delta + 1;
		int[] f = new int[len];
		// 统计出现的次数（频率）
		int findex = 0;
		for (int i = 0; i < n; i++) {
			// 取数组元素的第exponent位。
			// 比如exponent等于1，则取最后一位（个位）；exponent等于10，则取十位。
			// 如果超过位数，则计算下来也正好是0，起到补0的效果。
			findex = ((a[i] - min) / exponent) % radix;
			f[findex]++;
		}
		// 计算相对位移
		for (int i = 1; i < len; i++)
			f[i] += f[i - 1];
		// 反序输出到中间数组（这样确保稳定）
		int[] r = new int[n];
		for (int i = n - 1; i > -1; i--) {
			findex = ((a[i] - min) / exponent) % radix;
			r[--f[findex]] = a[i];
		}
		// 再拷贝回去
		for (int i = 0; i < n; i++)
			a[i] = r[i];
	}

	/**
	 * 桶排序：先使用计数排序进行分桶操作，然后对每个非空桶进行排序。
	 * 
	 * 非比较排序，又叫箱排序。
	 * 
	 * @param a
	 *            要排序的数组
	 */
	// 分类 ------------- 内部非比较排序
	// 数据结构 --------- 数组
	// 最差时间复杂度 ---- O(nlogn)或O(n^2)，只有一个桶，取决于桶内排序方式
	// 最优时间复杂度 ---- O(n)，每个元素占一个桶
	// 平均时间复杂度 ---- O(n)，保证各个桶内元素个数均匀即可
	// 所需辅助空间 ------ O(n + bn)
	// 稳定性 ----------- 稳定
	public static void bucketSort(int[] a) {
		checkArray(a);
		int n = a.length;
		int max = a[0];
		int min = max;
		for (int i : a) {
			if (i > max)
				max = i;
			if (i < min)
				min = i;
		}
		// 桶的容量
		int size = 1;
		int delta = max - min;
		int t = delta;
		while (t > 0) {
			size *= DECIMAL;
			t /= 10;
		}
		// 桶的数量
		int bs = n % size == 0 ? n / size : n / size + 1;
		// 记录桶的边缘
		int[] b = new int[bs];
		// 计数排序进行分桶
		countSortForBucket(a, b, size, min);
		// 每个桶进行单独排序
		for (int i = 0; i < bs; i++) {
			int start = b[i];
			int end = i == bs - 1 ? n - 1 : b[i + 1] - 1;
			// 这里使用选择排序，其他排序方法也是可以的
			selectSortForBucket(a, start, end);
		}
	}

	private static void countSortForBucket(int[] a, int[] bucket, int size, int min) {
		int n = a.length;
		int bn = bucket.length;
		int[] r = new int[n];
		// 统计出现的次数
		int findex = 0;
		for (int i = 0; i < n; i++) {
			findex = (a[i] - min) / size;
			bucket[findex]++;
		}
		// 计算相对位移
		for (int i = 1; i < bn; i++)
			bucket[i] += bucket[i - 1];
		// 倒序输出数组（稳定）
		for (int i = n - 1; i > -1; i--) {
			findex = (a[i] - min) / size;
			// 桶的边缘被更新：bucket[i]为第i号桶中第一个元素所在r中的位置
			r[--bucket[findex]] = a[i];
		}
		// 再拷贝回去
		for (int i = 0; i < n; i++)
			a[i] = r[i];
	}

	private static void selectSortForBucket(int[] a, int start, int end) {
		// 都是闭合区间
		for (int i = start; i < end; i++) {
			int min = i;
			for (int j = i + 1; j <= end; j++) {
				if (a[j] < a[min])
					min = j;
			}
			if (min != i) {
				swap(a, min, i);
			}
		}
	}

	/**
	 * 堆排序：利用数组来实现堆，利用堆的性质来排序。
	 * 
	 * 因为这个堆是使用数组实现的，所以元素节点不需要复杂操作的左孩子和右孩子，只是有数学上的关系而已。
	 * 
	 * 堆的含义是：完全二叉树中所有非叶子结点的值均不大于（或不小于）其左、右孩子结点的值。
	 * 
	 * @param a
	 *            要排序的数组
	 */
	// 分类 -------------- 内部比较排序
	// 数据结构 ---------- 数组
	// 最差时间复杂度 ---- O(nlogn)
	// 最优时间复杂度 ---- O(nlogn)
	// 平均时间复杂度 ---- O(nlogn)
	// 所需辅助空间 ------ O(1)
	// 稳定性 ------------ 不稳定
	public static void heapSort(int[] a) {
		checkArray(a);
		int n = a.length;
		// 构造初始大顶堆
		heapSortBuild(a);
		// 无序区的元素个数大于1，则代表未完成排序
		// n==1时即排序完成，最后一个元素无需调整
		while (n > 1) {
			// 每次将第一个元素（堆顶元素）和最后一个元素（叶子节点）交换
			// 交换不仅会破环原来的堆结构，也会改变相对位置，所以堆排序是非稳定的
			swap(a, 0, --n);
			// 从新的堆顶元素开始向下重新进行堆调整，使其成为一个新堆，时间复杂度O(logn)
			heapify(a, 0, n);
		}
	}

	// 构建大顶堆其实也是堆调整的过程
	private static void heapSortBuild(int[] a) {
		int n = a.length;
		// 从最后一个非叶子结点开始向上进行调整
		for (int i = n / 2 - 1; i > -1; i--) {
			heapify(a, i, n);
		}
	}

	// 堆调整，对大小为0-size的堆中第index个元素进行调整，使其满足大顶堆
	private static void heapify(int[] a, int index, int size) {
		// 该节点，节点的左孩子，节点的右孩子哪个最大
		int max = index;
		// 左孩子节点是该节点的2*index+1
		int left = 2 * index + 1;
		if (left < size && a[left] > a[max])
			max = left;
		// 右孩子节点是该节点的2*index+2
		int right = 2 * index + 2;
		if (right < size && a[right] > a[max])
			max = right;
		if (max != index) {
			// 将最大的节点调整为父节点
			swap(a, max, index);
			// 递归调用，继续将交换到max节点的被破坏部分进行调整
			heapify(a, max, size);
		}
	}

}
