package com.ynswet.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author elfmatian
 *
 */
public class ObjectUtil {
	/**
	 * 属性的第一个字母变为大写
	 *
	 * @param str
	 * @param method
	 * @return
	 */
	private static String upMethodCase(String str, String method) {
		char[] sbArr = str.toCharArray();
		sbArr[0] = String.valueOf(sbArr[0]).toUpperCase().charAt(0);
		String name = method + String.valueOf(sbArr);
		return name;
	}

	/**
	 * 通过field组装set和get方法
	 *
	 * @param fieldArr
	 * @return
	 */
	private static Map<String, String> fieldsMethodMap(Field[] fieldArr) {
		Map<String, String> map = new HashMap<String, String>();
		if (null != fieldArr && fieldArr.length > 0) {
			for (int i = 0; i < fieldArr.length; i++) {
				String name = fieldArr[i].getName();
				map.put(upMethodCase(name, "get"), upMethodCase(name, "set"));

			}
		}
		return map;
	}

	/**
	 * 将一个对象的所有内容重新Copy一份
	 *
	 * @param oldObj
	 * @param newObj
	 * @return
	 */
	public static Object getObjectCopy(Object oldObj, Object newObj) {

		Field[] fieldArr = oldObj.getClass().getDeclaredFields();
		Map<String, String> map = fieldsMethodMap(fieldArr);
		Method[] methodArr = oldObj.getClass().getDeclaredMethods();

		try {
			for (int j = 0; j < methodArr.length; j++) {
				String methodName = methodArr[j].getName();
				if (map.containsKey(methodName)) {
					Object obj = methodArr[j].invoke(oldObj);

					if (obj != null)
						try {
							Method newMethod = newObj.getClass().getMethod(
									(String) map.get(methodName),
									new Class[] { obj.getClass() });
							newMethod.invoke(newObj, new Object[] { obj });
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}

			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return newObj;
	}

	/**
	 * 判断null或array.length
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}

	/**
	 * null返回空字符串
	 * @param object
	 * @return
	 */
	public static Object parseNull(Object object) {
		return object == null ? "" : object;
	}
}
