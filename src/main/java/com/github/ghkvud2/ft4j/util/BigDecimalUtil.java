package com.github.ghkvud2.ft4j.util;

import java.math.BigDecimal;

public class BigDecimalUtil {

	public static String convert(Object value) {
		if (value instanceof Float) {
			return convert((float) value);
		} else if (value instanceof Double) {
			double doubleValue = (double) value;
			float floatValue = (float) doubleValue;
			if (Math.abs(doubleValue - floatValue) < 1e-7) {
				return convert(floatValue);
			} else {
				return convert(doubleValue);
			}
		} else {
			throw new IllegalArgumentException("Invalid type: " + value.getClass().getName());
		}
	}

	private static String convert(float value) {
		BigDecimal bd = new BigDecimal(Float.toString(value));
		bd = bd.stripTrailingZeros();
		return bd.toPlainString();
	}

	private static String convert(double value) {
		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.stripTrailingZeros();
		return bd.toPlainString();
	}
}
