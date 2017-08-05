package com.chain.test;

import java.io.IOException;

import org.junit.Test;

import com.chain.utils.FileVerifyUtils;

public class FileVerifyUtilsTest {

	// 文件校验
	@Test
	public void test2() throws IOException {
		String fileName = "E:\\Temps\\test.txt";
		String crc32 = FileVerifyUtils.verify(FileVerifyUtils.CRC32, fileName);
		String md5 = FileVerifyUtils.verify(FileVerifyUtils.MD5, fileName);
		String sha1 = FileVerifyUtils.verify(FileVerifyUtils.SHA1, fileName);
		System.out.println(crc32);
		System.out.println(md5);
		System.out.println(sha1);
	}
}
