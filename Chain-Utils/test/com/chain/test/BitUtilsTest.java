package com.chain.test;

import org.junit.Test;

import com.chain.utils.BitUtils;
import com.chain.utils.PrintUtils;
import com.chain.utils.ScaleConvertUtils;

public class BitUtilsTest {

	@Test
	public void test1() {
		// 10010
		int n1 = 18;
		test(n1);
	}

	@Test
	public void test2() {
		// 11111111111111111111111111101110
		int n1 = -18;
		test(n1);
	}

	private void test(int n1) {
		System.out.println(n1);
		System.out.println(ScaleConvertUtils.toBinaryString(n1));

		System.out.println();
		System.out.println(BitUtils.getRightK(n1, 2));

		System.out.println();
		int n2 = BitUtils.setRightKToZero(n1, 2);
		System.out.println(n2);
		System.out.println(ScaleConvertUtils.toBinaryString(n2));

		System.out.println();
		int n3 = BitUtils.setRightKToOne(n1, 3);
		System.out.println(n3);
		System.out.println(ScaleConvertUtils.toBinaryString(n3));

		System.out.println();
		int n4 = BitUtils.inverseRightK(n1, 3);
		System.out.println(n4);
		System.out.println(ScaleConvertUtils.toBinaryString(n4));

		System.out.println();
		int n5 = BitUtils.setLeftKToZero(n1, 4);
		System.out.println(n5);
		System.out.println(ScaleConvertUtils.toBinaryString(n5));

		System.out.println();
		int n6 = BitUtils.setLeftKToOne(n1, 3);
		System.out.println(n6);
		System.out.println(ScaleConvertUtils.toBinaryString(n6));

		System.out.println();
		int n7 = BitUtils.inverseLeftK(n1, 3);
		System.out.println(n7);
		System.out.println(ScaleConvertUtils.toBinaryString(n7));

		System.out.println();
		System.out.println(BitUtils.getMulPowerK(n1, 2));

		System.out.println();
		System.out.println(BitUtils.getDivPowerK(n1, 2));

		System.out.println();
		System.out.println(BitUtils.isEven(n1));
		System.out.println(BitUtils.isOdd(n1 - 1));

		System.out.println();
		int[] a1 = { 1, 2 };
		BitUtils.swap(a1, 1, 2);
		PrintUtils.show(a1);

		System.out.println();
		int n8 = BitUtils.opposite(-n1);
		System.out.println(n8);
		System.out.println(ScaleConvertUtils.toBinaryString(n8));

		System.out.println();
		int n9 = BitUtils.getModPowerK(n1, 4);
		System.out.println(n9);

		System.out.println();
		System.out.println(BitUtils.isPowerOfTwo(n1));

		System.out.println();
		int n10 = BitUtils.abs(-n1);
		System.out.println(n10);
		System.out.println(ScaleConvertUtils.toBinaryString(n10));

		System.out.println();
		System.out.println(BitUtils.isSameSignal(n1, -n2));

		System.out.println();
		int n11 = BitUtils.getRightFirstOneIndex(n1);
		System.out.println(n11);

		System.out.println();
		int n12 = BitUtils.setRightFirstOneToZero(n1);
		System.out.println(n12);
		System.out.println(ScaleConvertUtils.toBinaryString(n12));

		System.out.println();
		int n13 = BitUtils.setRightFirstZeroToOne(n1);
		System.out.println(n13);
		System.out.println(ScaleConvertUtils.toBinaryString(n13));

		System.out.println();
		int n14 = BitUtils.setRightContinuousOneToZero(n1 + 1);
		System.out.println(n14);
		System.out.println(ScaleConvertUtils.toBinaryString(n14));

		System.out.println();
		int n15 = BitUtils.setRightContinuousZeroToOne(n1 - 2);
		System.out.println(n15);
		System.out.println(ScaleConvertUtils.toBinaryString(n15));

		System.out.println();
		int n16 = BitUtils.getRightContinuousOne(n1 + 1);
		System.out.println(n16);
		System.out.println(ScaleConvertUtils.toBinaryString(n16));

		System.out.println();
		int n17 = BitUtils.removeRightContinuousZero(40);
		System.out.println(n17);
		System.out.println(ScaleConvertUtils.toBinaryString(n17));

		System.out.println();
		int n18 = BitUtils.getRightToK(n1, 2);
		System.out.println(n18);
		System.out.println(ScaleConvertUtils.toBinaryString(n18));

		System.out.println();
		int n19 = BitUtils.setRightToKAllToZero(n1, 2);
		System.out.println(n19);
		System.out.println(ScaleConvertUtils.toBinaryString(n19));

		System.out.println();
		int n20 = BitUtils.setRightToKAllToOne(n1, 2);
		System.out.println(n20);
		System.out.println(ScaleConvertUtils.toBinaryString(n20));

		System.out.println();
		int n21 = BitUtils.inverseRightToK(n1, 2);
		System.out.println(n21);
		System.out.println(ScaleConvertUtils.toBinaryString(n21));
	}

}
