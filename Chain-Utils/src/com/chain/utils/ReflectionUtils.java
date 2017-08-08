package com.chain.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chain.exception.ChainUtilsRuntimeException;

/**
 * 反射工具类
 * 
 * 可以获得类的方法和熟悉，执行方法，设置属性，泛型，注解，根据类生成实例
 * 
 * @author Chain Qian
 * @version 1.0
 *
 */
public class ReflectionUtils {

	private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

	/**
	 * 根据类名（className）获得对应的“类实例”（instance of Class）
	 * 
	 * @param className
	 *            类名
	 * @return 类实例
	 */
	public static Class getClassByName(String className) {
		Class clz = null;
		try {
			clz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			logger.error("getClassByName exception", e);
			throw new ChainUtilsRuntimeException("getClassByName exception", e);
		}
		return clz;
	}

	/**
	 * 根据对象（Object）获得对应的“类实例”（instance of Class）
	 * 
	 * @param obj
	 *            对象
	 * @return 类实例
	 */
	public static Class getClassByObject(Object obj) {
		return obj.getClass();
	}

	public static Method getDeclaredMethod(Object obj, String methodName, Class... parameterTypes) {
		return getDeclaredMethod(getClassByObject(obj), methodName, parameterTypes);
	}

	public static Method getDeclaredMethod(String className, String methodName, Class... parameterTypes) {
		return getDeclaredMethod(getClassByName(className), methodName, parameterTypes);
	}

	/**
	 * 获得类中对应的具有某个参数列表的方法
	 * 
	 * @param clz
	 *            类
	 * @param methodName
	 *            方法名
	 * @param parameterTypes
	 *            方法的参数列表的Class类型
	 * @return 方法
	 */
	public static Method getDeclaredMethod(Class clz, String methodName, Class... parameterTypes) {
		for (Class c = clz; c != Object.class; c = c.getSuperclass()) {
			try {
				Method m = c.getDeclaredMethod(methodName, parameterTypes);
				return m;
			} catch (NoSuchMethodException | SecurityException e) {
				// e.printStackTrace();
				continue;
			}
		}
		return null;
	}

	/**
	 * 忽略或者设置方法或者属性的修饰符（“暴力访问”）
	 * 
	 * @param obj
	 *            方法或属性
	 * @param bool
	 *            可访问
	 */
	private static void setAccessible(Object obj, boolean bool) {
		if (obj instanceof Method)
			((Method) obj).setAccessible(bool);
		else if (obj instanceof Field)
			((Field) obj).setAccessible(bool);
	}

	/**
	 * 根据类名获得类的实例（需要类具有无参构造函数）
	 * 
	 * @param className
	 *            类名称
	 * @return 实例
	 */
	public static Object getObjectByName(String className) {
		return getObjectByClass(getClassByName(className));
	}

	/**
	 * 根据“类实例”获得类的实例（需要类具有无参构造函数）
	 * 
	 * @param clz
	 *            类
	 * @return 实例
	 */
	public static Object getObjectByClass(Class clz) {
		Object obj = null;
		try {
			obj = clz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("getObjectByClass exception", e);
			throw new ChainUtilsRuntimeException("getObjectByClass exception", e);
		}
		return obj;
	}

	public static Object invokeMethod(String classToInvoke, Method m, Object... methodParams) {
		Object obj = getObjectByName(classToInvoke);
		return invokeMethod(obj, m, methodParams);
	}

	public static Object invokeMethod(Class classToInvoke, Method m, Object... methodParams) {
		Object obj = getObjectByClass(classToInvoke);
		return invokeMethod(obj, m, methodParams);
	}

	/**
	 * 执行对象的某个方法
	 * 
	 * @param objToInvoke
	 *            对象
	 * @param m
	 *            方法
	 * @param methodParams
	 *            方法的参数
	 * @return 方法的执行结果
	 */
	public static Object invokeMethod(Object objToInvoke, Method m, Object... methodParams) {
		Object result = null;
		try {
			setAccessible(m, true);
			result = m.invoke(objToInvoke, methodParams);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error("invoke method exception", e);
			throw new ChainUtilsRuntimeException("invoke method exception", e);
		}
		return result;
	}

	public static Field getDeclaredField(Object obj, String fieldName) {
		return getDeclaredField(getClassByObject(obj), fieldName);
	}

	public static Field getDeclaredField(String clzName, String fieldName) {
		return getDeclaredField(getClassByName(clzName), fieldName);
	}

	/**
	 * 获得类中的某个属性（字段）
	 * 
	 * @param clz
	 *            类
	 * @param fieldName
	 *            属性名称
	 * @return 属性
	 */
	public static Field getDeclaredField(Class clz, String fieldName) {
		for (Class c = clz; c != Object.class; c = c.getSuperclass()) {
			try {
				Field f = c.getDeclaredField(fieldName);
				return f;
			} catch (SecurityException | NoSuchFieldException e) {

				// e.printStackTrace();
				continue;
			}
		}
		return null;
	}

	public static Object setField(String clzName, Field f, Object value) {
		return setField(getObjectByName(clzName), f, value);
	}

	public static Object setField(Class clz, Field f, Object value) {
		return setField(getObjectByClass(clz), f, value);
	}

	/**
	 * 设置对象中某个属性的值
	 * 
	 * @param obj
	 *            对象
	 * @param f
	 *            属性
	 * @param value
	 *            值
	 * @return 对象本身
	 */
	public static Object setField(Object obj, Field f, Object value) {
		try {
			setAccessible(f, true);
			f.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			logger.error("setField exception", e);
			throw new ChainUtilsRuntimeException("setField exception", e);
		}
		return obj;
	}

	public static Constructor getConstructor(String clzName, Class... parameterTypes) {
		return getConstructor(getClassByName(clzName), parameterTypes);
	}

	public static Constructor getConstructor(Object obj, Class... parameterTypes) {
		return getConstructor(getClassByObject(obj), parameterTypes);
	}

	/**
	 * 获得类的某个构造器
	 * 
	 * @param clz
	 *            类
	 * @param parameterTypes
	 *            参数
	 * @return 构造方法
	 */
	public static Constructor getConstructor(Class clz, Class... parameterTypes) {
		for (Class c = clz; c != Object.class; c = c.getSuperclass()) {
			try {
				Constructor cs = c.getConstructor(parameterTypes);
				return cs;
			} catch (SecurityException | NoSuchMethodException e) {
				// e.printStackTrace();
				continue;
			}
		}
		return null;
	}

	/**
	 * 根据构造器创建类实例
	 * 
	 * @param cs
	 *            构造器
	 * @param initArgs
	 *            构造器参数
	 * @return 实例
	 */
	public static Object newInstanceByConstrutor(Constructor cs, Object... initArgs) {
		Object obj = null;
		try {
			obj = cs.newInstance(initArgs);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			logger.error("newInstanceByConstrutor exception", e);
			throw new ChainUtilsRuntimeException("newInstanceByConstrutor exception", e);
		}
		return obj;
	}

	/**
	 * 获得加在类上的注解
	 * 
	 * @param clz
	 *            类
	 * @param annotationClass
	 *            注解的类
	 * @return 注解
	 */
	public static Annotation getDeclaredAnnotation(Class clz, Class annotationClass) {
		Annotation an = clz.getDeclaredAnnotation(annotationClass);
		return an;
	}

	/**
	 * 获得加在属性上的注解
	 * 
	 * @param f
	 *            属性
	 * @param annotationClass
	 *            注解的类
	 * @return 注解
	 */
	public static Annotation getDeclaredAnnotation(Field f, Class annotationClass) {
		Annotation an = f.getDeclaredAnnotation(annotationClass);
		return an;
	}

	/**
	 * 获得加在方法上的注解
	 * 
	 * @param m
	 *            方法
	 * @param annotationClass
	 *            注解的类
	 * @return 注解
	 */
	public static Annotation getDeclaredAnnotation(Method m, Class annotationClass) {
		Annotation an = m.getDeclaredAnnotation(annotationClass);
		return an;
	}

	/**
	 * 获得加在构造器上的注解
	 * 
	 * @param c
	 *            构造器
	 * @param annotationClass
	 *            注解的类
	 * @return 注解
	 */
	public static Annotation getDeclaredAnnotation(Constructor c, Class annotationClass) {
		Annotation an = c.getDeclaredAnnotation(annotationClass);
		return an;
	}

	/**
	 * 获得类（或父类）的泛型参数类型中的某一个
	 * 
	 * @param clz
	 *            类
	 * @param index
	 *            泛型参数下标
	 * @return 泛型参数的类
	 */
	public static Class getSuperGenericType(Class clz, int index) {
		List<Class> lst = getSuperGenericTypes(clz);
		if (lst.size() - 1 < index)
			return Object.class;
		return lst.get(index);
	}

	/**
	 * 
	 * 获得类（或父类）的泛型参数类型的列表
	 * 
	 * @param clz
	 *            类
	 * @return 泛型参数列表
	 */
	public static List<Class> getSuperGenericTypes(Class clz) {
		List<Class> lst = new ArrayList<>();

		if (clz == null) {
			lst.add(Object.class);
			return lst;
		}

		Type genericType = clz.getGenericSuperclass();

		if (!(genericType instanceof ParameterizedType)) {
			lst.add(Object.class);
			return lst;
		}

		Type[] params = ((ParameterizedType) genericType).getActualTypeArguments();

		for (int i = 0; i < params.length; i++)
			if (params[i] instanceof Class)
				lst.add((Class) params[i]);
			else
				lst.add(Object.class);

		return lst;
	}

}
