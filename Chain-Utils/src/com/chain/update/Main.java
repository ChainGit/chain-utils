package com.chain.update;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		// PrintUtils.show(args);
		if (args.length > 0) {
			if ("-c".equals(args[0])) {
				if (ChainUtilsUpdate.check())
					return;
			} else if ("-u".equals(args[0])) {
				if (args.length == 1) {
					ChainUtilsUpdate.update();
				} else if (args.length == 2) {
					ChainUtilsUpdate.update(args[1]);
				}
				return;
			}
		} else {
			showUsage();
		}
	}

	/**
	 * 打印如何使用
	 * 
	 */
	private static void showUsage() {
		System.out.println("this is a tool&utils.");
		System.out.println();
		System.out.println("-c		check if this local jar is latest.");
		System.out.println(
				"-u		update if this local jar is not the latest and download the latest one to current path.");
		System.out.println(
				"-u [FILE_PATH]	update if this local jar is not the latest and download the latest one to the FILE_PATH.");
	}

}
