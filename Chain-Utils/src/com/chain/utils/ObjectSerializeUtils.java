package com.chain.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Java序列化方式复制一个对象
 * 
 * @author Chain Qian
 * @version 1.0
 *
 */
public class ObjectSerializeUtils {

	/**
	 * 通过序列化复制一个对象(完全拷贝，速度较慢)
	 * 
	 * @param orig
	 *            要复制的对象
	 * @return 复制的对象
	 */
	public static Object copy(Object orig) {
		Object obj = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(orig);
			out.flush();
			out.close();

			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
			obj = in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return obj;
	}

}
