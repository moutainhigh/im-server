package org.chl.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5密码工具
 * @author chenjie
 * @date 2016年6月28日上午10:35:58
 * @since jdk 1.8
 * 
 * @version 1.0
 */
public class Md5Util {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Md5Util.class);
	
	public static String encode(String source) {
		try {
			StringBuffer result = new StringBuffer();
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bs = md.digest(source.getBytes());
			for (byte b : bs) {
				if (b < 0) {
					result.append(String.format("%2s", Integer.toHexString(256 + b)));
				} else {
					result.append(String.format("%2s", Integer.toHexString(b)));
				}
			}
			return result.toString().replaceAll("\\s", "0");
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("md5 error");
			return source;
		}
	}
}