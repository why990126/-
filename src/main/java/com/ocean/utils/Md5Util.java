package com.ocean.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;

public final class Md5Util {
	/**
	 * 对指定的字符串使用md5加密，返回加密以后的字符串
	 * @param str 要加密的字符串
	 * @return 加密以后的字符串
	 */
	public static String getMD5(String str) {
		try {
			//1. 使用静态方法，得到一个md5信息摘要的对象
			MessageDigest md = MessageDigest.getInstance("MD5");
			//2. 将字符串使用utf-8进行编码，得到字节数组
			byte[] input = str.getBytes("utf-8");
			System.out.println("utf-8编码的字节数组：" + Arrays.toString(input));
			// 3. 对字节数组进行md5的哈希计算，得到加密以后的字节数组，长度是16个字节。
			byte[] m = md.digest(input); //加密完成了
			System.out.println("通过hash计算以后得到的字节数组：" + Arrays.toString(m));
			/*
			 * 4. 将这个二进制数组转成无符号的大整数
			 * 参数1：1表示这是一个正数，-1表示负数
			 * 参数2：要转换的二进制数组
			 */
			BigInteger big = new BigInteger(1, m);
			System.out.println("转成无符号的大整数：" + big);
			//5. 将这个大整数转成16进制字符串，参数为多少进制
			String hex = big.toString(16);
			return hex;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("MD5加密出现错误");
		}
	}

	public static void main(String[] args) {
		String md51 = getMD5("123fsdafafaffsadf");
		String md52 = getMD5("124");
		System.out.println(md51); // 13a024acccd35d2d23562aeb6dd73a2a
		System.out.println(md52); // 202cb962ac59075b964b07152d234b70
	}

}
