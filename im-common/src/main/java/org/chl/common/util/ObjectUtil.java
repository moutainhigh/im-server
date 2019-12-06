package org.chl.common.util;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * 
 * @author chenjie
 * @date 2019年4月24日 下午4:14:22
 * @since 1.8
 * 
 * @version 1.0
 */
public class ObjectUtil {
	
	public static Object from(byte[] bytes) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T from(byte[] bytes, Class<T> clazz) {
		Object obj = from(bytes);
		if (null != obj && obj.getClass() == clazz) {
			return (T) obj;
		}
		return null;
	}
	
	private ObjectUtil() {
		super();
	}
}