package com.chain.utils;

/**
 * 打印到控制台
 * 
 * @author Chain Qian
 * @version 1.0
 *
 */
public class PrintUtils {

	public static void show(int[] objs) {
		for (int o : objs)
			System.out.print(o + " ");
		System.out.println();
	}

	public static void show(byte[] objs) {
		for (byte o : objs)
			System.out.print(o + " ");
		System.out.println();
	}

	public static void show(long[] objs) {
		for (long o : objs)
			System.out.print(o + " ");
		System.out.println();
	}

	public static void show(short[] objs) {
		for (short o : objs)
			System.out.print(o + " ");
		System.out.println();
	}

	public static void show(float[] objs) {
		for (float o : objs)
			System.out.print(o + " ");
		System.out.println();
	}

	public static void show(double[] objs) {
		for (double o : objs)
			System.out.print(o + " ");
		System.out.println();
	}

	public static void show(Object[] objs) {
		for (Object o : objs)
			System.out.print(o + " ");
		System.out.println();
	}
}
