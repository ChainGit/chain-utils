package com.chain.test;

import org.junit.Test;

import com.chain.utils.FloatDoubleConvertUtils;
import com.chain.utils.ScaleConvertUtils;

public class FloatDoubleConvertUtilsTest {

	@Test
	public void test1() throws Exception {
		String s = FloatDoubleConvertUtils.toBinaryString(0f);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(0f));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToFloat(s));

		System.out.println();

		System.out.println();

		s = FloatDoubleConvertUtils.toBinaryString(-0f);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(-0f));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToFloat(s));

		System.out.println();

		s = FloatDoubleConvertUtils.toBinaryString(1.0f);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(1.0f));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToFloat(s));

		System.out.println();

		s = FloatDoubleConvertUtils.toBinaryString(2.5f);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(2.5f));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToFloat(s));

		System.out.println();

		s = FloatDoubleConvertUtils.toBinaryString(-2.5f);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(-2.5f));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToFloat(s));

		System.out.println();

		s = FloatDoubleConvertUtils.toBinaryString(-12.163f);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(-12.163f));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToFloat(s));

		System.out.println();

		s = FloatDoubleConvertUtils.toBinaryString(0.163f);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(0.163f));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToFloat(s));

		System.out.println();

		s = FloatDoubleConvertUtils.toBinaryString(0.5f);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(0.5f));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToFloat(s));

		System.out.println();

		s = FloatDoubleConvertUtils.toBinaryString(-0.5f);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(-0.5f));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToFloat(s));

		System.out.println();
	}

	@Test
	public void test2() throws Exception {
		String s = FloatDoubleConvertUtils.toBinaryString(0);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(0));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToFloat(s));

		System.out.println();

		System.out.println();

		s = FloatDoubleConvertUtils.toBinaryString(-0);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(-0));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToDouble(s));

		System.out.println();

		s = FloatDoubleConvertUtils.toBinaryString(1.0);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(1.0));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToDouble(s));

		System.out.println();

		s = FloatDoubleConvertUtils.toBinaryString(2.5);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(2.5));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToDouble(s));

		System.out.println();

		s = FloatDoubleConvertUtils.toBinaryString(-2.5);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(-2.5));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToDouble(s));

		System.out.println();

		s = FloatDoubleConvertUtils.toBinaryString(-12.163);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(-12.163));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToDouble(s));

		System.out.println();

		s = FloatDoubleConvertUtils.toBinaryString(0.163);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(0.163));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToDouble(s));

		System.out.println();

		s = FloatDoubleConvertUtils.toBinaryString(0.5);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(0.5));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToDouble(s));

		System.out.println();

		s = FloatDoubleConvertUtils.toBinaryString(-0.5);
		System.out.println(s);
		System.out.println(ScaleConvertUtils.toBinaryString(-0.5));
		System.out.println(FloatDoubleConvertUtils.parseBinaryStringToDouble(s));

		System.out.println();
	}

	@Test
	public void test3() throws Exception {
		double d1 = Math.random();
		System.out.println(d1);
		String s1 = FloatDoubleConvertUtils.toBinaryString(d1);
		System.out.println(s1);
		double d2 = FloatDoubleConvertUtils.parseBinaryStringToDouble(s1);
		System.out.println(d2);

		double d3 = d1 * ((int) (Math.random() * 100000));
		System.out.println(d3);
		String s2 = FloatDoubleConvertUtils.toBinaryString(d3);
		System.out.println(s2);
		double d4 = FloatDoubleConvertUtils.parseBinaryStringToDouble(s2);
		System.out.println(d4);

		double d5 = d3 - ((int) (Math.random() * 100000));
		System.out.println(d5);
		String s3 = FloatDoubleConvertUtils.toBinaryString(d5);
		System.out.println(s3);
		double d6 = FloatDoubleConvertUtils.parseBinaryStringToDouble(s3);
		System.out.println(d6);
	}
}
