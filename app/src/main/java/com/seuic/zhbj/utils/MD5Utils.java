package com.seuic.zhbj.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Utils {
	public static String digest(String content){
		StringBuilder sb=new StringBuilder();
		try {
			//第一次加密
			MessageDigest messageDigest=MessageDigest.getInstance("MD5");
			byte[] digest = messageDigest.digest(content.getBytes());
			for (int i = 0; i < digest.length; i++) {
				//第二次加密
				int result=digest[i] & 0xff;
				String hexString = Integer.toHexString(result);
				
				if (hexString.length()<2) {
					sb.append(0);
				}
				sb.append(hexString);
			}
			return sb.toString();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
