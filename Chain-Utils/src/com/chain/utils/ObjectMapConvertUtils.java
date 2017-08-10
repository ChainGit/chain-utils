package com.chain.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.chain.logging.ChainUtilsLoggerFactory;

/**
 * Java对象和Map之间的互转
 * 
 * @author Chain Qian
 * @version 1.0
 *
 */
public class ObjectMapConvertUtils {

	private static final Logger logger = ChainUtilsLoggerFactory.getLogger(ObjectMapConvertUtils.class);

	/**
	 * map转object
	 * 
	 * @param map
	 *            Map信息
	 * @param beanClass
	 *            要转成的类
	 * @param filters
	 *            过滤哪些属性(字段)不用复制
	 * @param <T>
	 *            类的类型
	 * @return 转换成对象
	 * @throws Exception
	 *             异常
	 */
	public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass, Set<String> filters) throws Exception {
		if (map == null)
			return null;

		try {
			Object obj = beanClass.newInstance();

			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				Method setter = property.getWriteMethod();
				if (setter != null) {
					String name = property.getName();
					if (filters == null || !filters.contains(name)) {
						setter.invoke(obj, map.get(name));
					}
				}
			}

			return (T) obj;
		} catch (Exception e) {
			logger.error("map convert object exception", e);
			throw e;
		}
	}

	/**
	 * object转map
	 * 
	 * @param obj
	 *            要转换对象
	 * @return 对象的Map形式
	 * @throws Exception
	 *             异常
	 */
	public static Map<String, Object> objectToMap(Object obj) throws Exception {
		if (obj == null)
			return null;

		try {
			Map<String, Object> map = new HashMap<String, Object>();

			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				if (key.compareToIgnoreCase("class") == 0) {
					continue;
				}
				Method getter = property.getReadMethod();
				Object value = getter != null ? getter.invoke(obj) : null;
				map.put(key, value);
			}

			return map;
		} catch (Exception e) {
			logger.error("object convert map exception", e);
			throw e;
		}
	}
}
