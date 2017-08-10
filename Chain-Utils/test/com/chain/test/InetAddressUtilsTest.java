package com.chain.test;

import org.junit.Test;

import com.chain.utils.Inet4AddressUtils;

public class InetAddressUtilsTest {

	@Test
	public void test1() {
		String ip1 = "192.168.10.1";
		String ip2 = "292.168.10.1";

		boolean b1 = Inet4AddressUtils.isInet4Address(ip1);
		System.out.println(b1);
		boolean b2 = Inet4AddressUtils.isInet4Address(ip2);
		System.out.println(b2);

		long iip1 = Inet4AddressUtils.parseString(ip1);
		System.out.println(iip1);

		String sip1 = Inet4AddressUtils.toFormatBinaryString(ip1);
		System.out.println(sip1);
		String sip2 = Inet4AddressUtils.toOctalString(ip1);
		System.out.println(sip2);
		String sip3 = Inet4AddressUtils.toFormatHexString(ip1);
		System.out.println(sip3);

		String sip4 = Inet4AddressUtils.toInet4Address(iip1);
		System.out.println(sip4);
		String sip5 = Inet4AddressUtils.toFormatInet4Address(iip1);
		System.out.println(sip5);
	}

	@Test
	public void test2() {
		String ip1 = "10.0.0.255";
		String ip2 = "10.0.0.256";

		boolean b1 = Inet4AddressUtils.isInet4Address(ip1);
		System.out.println(b1);
		boolean b2 = Inet4AddressUtils.isInet4Address(ip2);
		System.out.println(b2);

		long iip1 = Inet4AddressUtils.parseString(ip1);
		System.out.println(iip1);

		String sip1 = Inet4AddressUtils.toFormatBinaryString(ip1);
		System.out.println(sip1);
		String sip2 = Inet4AddressUtils.toOctalString(ip1);
		System.out.println(sip2);
		String sip3 = Inet4AddressUtils.toFormatHexString(ip1);
		System.out.println(sip3);

		String sip4 = Inet4AddressUtils.toInet4Address(iip1);
		System.out.println(sip4);
		String sip5 = Inet4AddressUtils.toFormatInet4Address(iip1);
		System.out.println(sip5);
	}

	@Test
	public void test3() {
		String ip1 = "0.0.0.0";

		boolean b1 = Inet4AddressUtils.isInet4Address(ip1);
		System.out.println(b1);

		long iip1 = Inet4AddressUtils.parseString(ip1);
		System.out.println(iip1);

		String sip1 = Inet4AddressUtils.toFormatBinaryString(ip1);
		System.out.println(sip1);
		String sip2 = Inet4AddressUtils.toOctalString(ip1);
		System.out.println(sip2);
		String sip3 = Inet4AddressUtils.toFormatHexString(ip1);
		System.out.println(sip3);

		String sip4 = Inet4AddressUtils.toInet4Address(iip1);
		System.out.println(sip4);
		String sip5 = Inet4AddressUtils.toFormatInet4Address(iip1);
		System.out.println(sip5);
	}

	@Test
	public void test4() {
		String ip1 = "255.255.255.255";

		boolean b1 = Inet4AddressUtils.isInet4Address(ip1);
		System.out.println(b1);

		long iip1 = Inet4AddressUtils.parseString(ip1);
		System.out.println(iip1);

		String sip1 = Inet4AddressUtils.toFormatBinaryString(ip1);
		System.out.println(sip1);
		String sip2 = Inet4AddressUtils.toOctalString(ip1);
		System.out.println(sip2);
		String sip3 = Inet4AddressUtils.toFormatHexString(ip1);
		System.out.println(sip3);

		String sip4 = Inet4AddressUtils.toInet4Address(iip1);
		System.out.println(sip4);
		String sip5 = Inet4AddressUtils.toFormatInet4Address(iip1);
		System.out.println(sip5);
	}

}
