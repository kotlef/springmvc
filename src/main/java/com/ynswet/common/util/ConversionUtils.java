/**
* Copyright 2007 - 2011 Skyway Software, Inc.
*/
package com.ynswet.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConversionUtils {
	public static <T> T safeConvert(Object sourceValue, Class<T> targetType) {
		try {
			return convert(sourceValue, targetType);
		} catch (Exception x) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T convert(Object sourceValue, Class<T> targetType) {
		Object convertedValue = null;
		T castValue;

		if (sourceValue == null)
			return null;

		if (targetType.isInstance(sourceValue)) {
			return targetType.cast(sourceValue);
		}

		if (targetType.isPrimitive()) {
			return (T) convert(sourceValue, getPrimitiveWrapper(targetType));
		}

		if (String.class.isAssignableFrom(targetType)) {
			return targetType.cast(sourceValue.toString());
		} else if (isInteger(targetType)) {
			convertedValue = convertToInteger(sourceValue);
		} else if (isDouble(targetType)) {
			convertedValue = convertToDouble(sourceValue);
		} else if (isFloat(targetType)) {
			convertedValue = convertToFloat(sourceValue);
		} else if (isLong(targetType)) {
			convertedValue = convertToLong(sourceValue);
		} else if (isShort(targetType)) {
			convertedValue = convertToShort(sourceValue);
		} else if (isByte(targetType)) {
			convertedValue = convertToByte(sourceValue);
		} else if (BigDecimal.class.isAssignableFrom(targetType)) {
			convertedValue = convertToBigDecimal(sourceValue);
		} else if (BigInteger.class.isAssignableFrom(targetType)) {
			convertedValue = convertToBigInteger(sourceValue);
		} else if (isBoolean(targetType)) {
			convertedValue = convertToBoolean(sourceValue);
		} else if (Date.class.isAssignableFrom(targetType)) {
			convertedValue = convertToDate(sourceValue);
		} else if (Calendar.class.isAssignableFrom(targetType)) {
			convertedValue = convertToCalendar(sourceValue);
		}

		if (convertedValue != null) {
			castValue = targetType.cast(convertedValue);
		} else {
			throw new RuntimeException("Cannot convert object of type [" + sourceValue.getClass().getName() + "] to instance of type [" + targetType.getClass().getName() + "].");
		}

		return castValue;
	}

	public static boolean needsConversion(Class<?> sourceType, Class<?> targetType) {
		if (targetType.isPrimitive()) {
			targetType = getPrimitiveWrapper(targetType);
		}

		if (sourceType.isPrimitive()) {
			sourceType = getPrimitiveWrapper(sourceType);
		}

		if (targetType.isAssignableFrom(sourceType)) {
			return false;
		}

		return true;
	}

	public static boolean isInstance(Object sourceValue, Class<?> targetType) {
		boolean result = targetType.isInstance(sourceValue);

		if (result)
			return true;

		return targetType.isPrimitive() && getPrimitiveWrapper(targetType) != null;
	}

	public static boolean isInteger(Class<?> targetType) {
		return Integer.class.isAssignableFrom(targetType) || Integer.TYPE.isAssignableFrom(targetType);
	}

	public static Integer convertToInteger(Object sourceValue) {
		if (sourceValue instanceof Number) {
			return new Integer(((Number) sourceValue).intValue());
		} else if (sourceValue instanceof String) {
			return Integer.parseInt((String) sourceValue);
		}

		return null;
	}

	public static boolean isDouble(Class<?> targetType) {
		return Double.class.isAssignableFrom(targetType) || Double.TYPE.isAssignableFrom(targetType);
	}

	public static Double convertToDouble(Object sourceValue) {
		if (sourceValue instanceof Number) {
			return new Double(((Number) sourceValue).doubleValue());
		} else if (sourceValue instanceof String) {
			return Double.parseDouble((String) sourceValue);
		}

		return null;
	}

	public static boolean isFloat(Class<?> targetType) {
		return Float.class.isAssignableFrom(targetType) || Float.TYPE.isAssignableFrom(targetType);
	}

	public static Float convertToFloat(Object sourceValue) {
		if (sourceValue instanceof Number) {
			return new Float(((Number) sourceValue).floatValue());
		} else if (sourceValue instanceof String) {
			return Float.parseFloat((String) sourceValue);
		}

		return null;
	}

	public static boolean isLong(Class<?> targetType) {
		return Long.class.isAssignableFrom(targetType) || Long.TYPE.isAssignableFrom(targetType);
	}

	public static Long convertToLong(Object sourceValue) {
		if (sourceValue instanceof Number) {
			return new Long(((Number) sourceValue).longValue());
		} else if (sourceValue instanceof String) {
			return Long.parseLong((String) sourceValue);
		}

		return null;
	}

	public static boolean isShort(Class<?> targetType) {
		return Short.class.isAssignableFrom(targetType) || Short.TYPE.isAssignableFrom(targetType);
	}

	public static Short convertToShort(Object sourceValue) {
		if (sourceValue instanceof Number) {
			return new Short(((Number) sourceValue).shortValue());
		} else if (sourceValue instanceof String) {
			return Short.parseShort((String) sourceValue);
		}

		return null;
	}

	public static boolean isByte(Class<?> targetType) {
		return Byte.class.isAssignableFrom(targetType) || Byte.TYPE.isAssignableFrom(targetType);
	}

	public static Byte convertToByte(Object sourceValue) {
		if (sourceValue instanceof Number) {
			return new Byte(((Number) sourceValue).byteValue());
		} else if (sourceValue instanceof String) {
			return Byte.parseByte((String) sourceValue);
		}

		return null;
	}

	public static BigDecimal convertToBigDecimal(Object sourceValue) {
		if (sourceValue instanceof Number) {
			return new BigDecimal(((Number) sourceValue).toString());
		} else if (sourceValue instanceof String) {
			return new BigDecimal((String) sourceValue);
		}

		return null;
	}

	public static BigInteger convertToBigInteger(Object sourceValue) {
		if (sourceValue instanceof Number) {
			return new BigInteger(((Number) sourceValue).toString());
		} else if (sourceValue instanceof String) {
			return new BigInteger((String) sourceValue);
		}

		return null;
	}

	public static boolean isBoolean(Class<?> targetType) {
		return Boolean.class.isAssignableFrom(targetType) || Boolean.TYPE.isAssignableFrom(targetType);
	}

	public static Boolean convertToBoolean(Object sourceValue) {
		if (sourceValue instanceof Number) {
			return Boolean.valueOf(((Number) sourceValue).longValue() == 1);
		} else if (sourceValue instanceof String) {
			return Boolean.valueOf((String) sourceValue);
		}

		return null;
	}

	public static Date convertToDate(Object sourceValue) {
		if (sourceValue instanceof Number) {
			return new Date(((Number) sourceValue).longValue());
		} else if (sourceValue instanceof Calendar) {
			return ((Calendar) sourceValue).getTime();
		} else if (sourceValue instanceof String) {
			return convertStringToDate((String) sourceValue);
		}

		return null;
	}

	public static Calendar convertToCalendar(Object sourceValue) {
		Calendar calendar = null;

		if (sourceValue instanceof Number) {
			calendar = Calendar.getInstance();
			calendar.setTimeInMillis(((Number) sourceValue).longValue());
		} else if (sourceValue instanceof Date) {
			calendar = Calendar.getInstance();
			calendar.setTime((Date) sourceValue);
		} else if (sourceValue instanceof String) {
			calendar = Calendar.getInstance();
			calendar.setTime(convertStringToDate((String) sourceValue));
		}

		return calendar;
	}

	private static Date convertStringToDate(String dateText) {
		DateFormat dateFormat = new SimpleDateFormat();
		try {
			return dateFormat.parse(dateText);
		} catch (ParseException x) {
			throw new RuntimeException("Unable to parse string [" + dateText + "] into date.", x);
		}
	}

	public static Class<?> getPrimitiveWrapper(Class<?> primitiveClass) {
		if (Boolean.TYPE.isAssignableFrom(primitiveClass)) {
			return Boolean.class;
		} else if (Byte.TYPE.isAssignableFrom(primitiveClass)) {
			return Byte.class;
		} else if (Character.TYPE.isAssignableFrom(primitiveClass)) {
			return Character.class;
		} else if (Short.TYPE.isAssignableFrom(primitiveClass)) {
			return Short.class;
		} else if (Integer.TYPE.isAssignableFrom(primitiveClass)) {
			return Integer.class;
		} else if (Long.TYPE.isAssignableFrom(primitiveClass)) {
			return Long.class;
		} else if (Float.TYPE.isAssignableFrom(primitiveClass)) {
			return Float.class;
		} else {
			return null;
		}
	}

	public static boolean isPrimitiveWrapper(Class<?> classToCheck) {
		return Boolean.class.isAssignableFrom(classToCheck) || Byte.class.isAssignableFrom(classToCheck) || Character.class.isAssignableFrom(classToCheck) || Short.class.isAssignableFrom(classToCheck) || Integer.class.isAssignableFrom(classToCheck) || Long.class.isAssignableFrom(classToCheck) || Float.class.isAssignableFrom(classToCheck) || Double.class.isAssignableFrom(classToCheck);
	}
}