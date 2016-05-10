package com.xfz.mobilesafe.utils;

/** 
 * @author xfz:xfz1990@gmail.com 
 * @version create time：2016-5-9 
 * specification
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	/**
	 * md5 encoding
	 * 
	 * @param password
	 * @return
	 */
	public static String encode(String password) {
		try {
			MessageDigest instance = MessageDigest.getInstance("MD5");
			byte[] digest = instance.digest(password.getBytes());

			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				int i = b & 0xff;
				String hexString = Integer.toHexString(i);

				if (hexString.length() < 2) {
					hexString = "0" + hexString;
				}

				sb.append(hexString);
			}

			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}
}
