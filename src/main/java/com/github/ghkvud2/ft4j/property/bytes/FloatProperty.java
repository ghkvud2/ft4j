package com.github.ghkvud2.ft4j.property.bytes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.github.ghkvud2.ft4j.annotation.FloatValue;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.exception.UnSupportedTypeException;
import com.github.ghkvud2.ft4j.property.AbstractProperty;
import com.github.ghkvud2.ft4j.util.BigDecimalUtil;

public class FloatProperty extends AbstractProperty<Float> implements ByteProperty {

	public FloatProperty(Object obj, Field field) {
		super(obj, field);
	}

	public FloatProperty(Field field) {
		super(field);
	}

	@Override
	public int length() {
		return ((FloatValue) annotation).length();
	}

	@Override
	public String defaultValue() {
		return ((FloatValue) annotation).defaultValue();
	}

	@Override
	public PaddingByte padding() {
		return ((FloatValue) annotation).paddingByte();
	}

	@Override
	public Justify justify() {
		return ((FloatValue) annotation).justify();
	}

	@Override
	public boolean ignoreLimit() {
		return ((FloatValue) annotation).ignoreLimit();
	}

	@Override
	protected Class<? extends Annotation> getAnnotationType() {
		return FloatValue.class;
	}

	@Override
	public String convertToString() {

		int fractionalLength = ((FloatValue) annotation).fractionalLength();
		if (fractionalLength < 0) {
			return BigDecimalUtil.convert(fieldValue);
		}
		int scaler = (int) Math.pow(10, fractionalLength);
		fieldValue = (float) (Math.floor(fieldValue * scaler) / scaler);
		String formatSpecifier = String.format("%%.%df", fractionalLength);
		String formattedDouble = String.format(formatSpecifier, fieldValue);
		return formattedDouble;
	}

	@Override
	public int fractionalLength() {
		return ((FloatValue) annotation).fractionalLength();
	}

	@Override
	public Float getFieldValue() {
		return this.fieldValue;
	}

	@Override
	public void setFieldValueFromString(String value) {
		this.fieldValue = Float.parseFloat(value);
	}

	@Override
	public void setFieldValueFromObject() {
		try {
			Object value = field.get(this.obj);
			this.fieldValue = value == null ? 0f : (float) value;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("Error: Unable to get field value.", e);
		}catch (ClassCastException e) {
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
		this.fieldValue = (float) value;
	}
}
