package com.chain.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 打印到控制台
 * 
 * @author Chain Qian
 * @version 1.1
 *
 */
public class PrintUtils {

	private static final Logger logger = LoggerFactory.getLogger(PrintUtils.class);

	public static void show(Object obj) {
		System.out.println(obj);
	}

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
