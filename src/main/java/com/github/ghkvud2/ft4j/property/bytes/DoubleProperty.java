package com.github.ghkvud2.ft4j.property.bytes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.github.ghkvud2.ft4j.annotation.DoubleValue;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.exception.UnSupportedTypeException;
import com.github.ghkvud2.ft4j.property.AbstractProperty;
import com.github.ghkvud2.ft4j.util.BigDecimalUtil;

public class DoubleProperty extends AbstractProperty<Double> implements ByteProperty {

	public DoubleProperty(Object obj, Field field) {
		super(obj, field);
	}

	public DoubleProperty(Field field) {
		super(field);
	}

	@Override
	public int length() {
		return ((DoubleValue) annotation).length();
	}

	@Override
	public String defaultValue() {
		return ((DoubleValue) annotation).defaultValue();
	}

	@Override
	public PaddingByte padding() {
		return ((DoubleValue) annotation).paddingByte();
	}

	@Override
	public Justify justify() {
		return ((DoubleValue) annotation).justify();
	}

	@Override
	public boolean ignoreLimit() {
		return ((DoubleValue) annotation).ignoreLimit();
	}

	@Override
	protected Class<? extends Annotation> getAnnotationType() {
		return DoubleValue.class;
	}

	@Override
	public String convertToString() {

		int fractionalLength = ((DoubleValue) annotation).fractionalLength();
		if (fractionalLength < 0) {
			return BigDecimalUtil.convert(fieldValue);
		}
		double scaler = Math.pow(10, fractionalLength);
		fieldValue = Math.floor(fieldValue * scaler) / scaler;
		String formatSpecifier = String.format("%%.%df", fractionalLength);
		String formattedDouble = String.format(formatSpecifier, fieldValue);
		return formattedDouble;
	}

	@Override
	public int fractionalLength() {
		return ((DoubleValue) annotation).fractionalLength();
	}

	@Override
	public Double getFieldValue() {
		return this.fieldValue;
	}

	@Override
	public void setFieldValueFromString(String value) {
		this.fieldValue = Double.parseDouble(value);
	}

	@Override
	public void setFieldValueFromObject() {
		try {
			Object value = field.get(this.obj);
			String temp = value == null ? "0" : BigDecimalUtil.convert(value);
			this.fieldValue = Double.valueOf(temp);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("Error: Unable to get field value.", e);
		} catch (ClassCastException e) {
			throw new UnSupportedTypeException(String.format("%s does not support %s type.",
					this.getClass().getSimpleName(), field.getType().getSimpleName()), e);
		}
	}

	@Override
	public void setFieldValueFromBytes(byte[] bytes) {

		double value = 0;
		double fraction = 0;
		boolean isFraction = false;
		int fractionDigits = 0;

		for (byte b : bytes) {
			if (b == '.') {
				isFraction = true;
			} else {
				int asciiValue = Character.getNumericValue(b);

				if (isFraction) {
					fraction = (fraction * 10) + asciiValue;
					fractionDigits++;
				} else {
					value = (value * 10) + asciiValue;
				}
			}
		}
		value = value + fraction / Math.pow(10, fractionDigits);
		this.fieldValue = value;
	}
}
