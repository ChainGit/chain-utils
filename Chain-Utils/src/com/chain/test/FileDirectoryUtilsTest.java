package com.chain.test;

import java.io.IOException;

import org.junit.Test;

import com.chain.utils.FileDirectoryUtils;

public class FileDirectoryUtilsTest {

	@Test
	public void test1() {
		FileDirectoryUtils fdu = FileDirectoryUtils.getInstance();
		fdu.deleteDirectory("E:\\Temps\\网页定位导航设计");
	}

	@Test
	public void test2() throws IOException {
		FileDirectoryUtils fdu = FileDirectoryUtils.getInstance();
		fdu.copyDirectory("E:\\Temps\\简化版百度翻译", "E:\\Temps\\简化版百度翻译2");
	}
}
