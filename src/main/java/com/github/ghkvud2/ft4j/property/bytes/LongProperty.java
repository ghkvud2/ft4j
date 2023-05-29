package com.github.ghkvud2.ft4j.property.bytes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.github.ghkvud2.ft4j.annotation.LongValue;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.exception.UnSupportedTypeException;
import com.github.ghkvud2.ft4j.property.AbstractProperty;

public class LongProperty extends AbstractProperty<Long> implements ByteProperty {

	public LongProperty(Object obj, Field field) {
		super(obj, field);
	}

	public LongProperty(Field field) {
		super(field);
	}

	@Override
	public int length() {
		return ((LongValue) annotation).length();
	}

	@Override
	public String defaultValue() {
		return ((LongValue) annotation).defaultValue();
	}

	@Override
	public PaddingByte padding() {
		return ((LongValue) annotation).paddingByte();
	}

	@Override
	public Justify justify() {
		return ((LongValue) annotation).justify();
	}

	@Override
	public boolean ignoreLimit() {
		return ((LongValue) annotation).ignoreLimit();
	}

	@Override
	protected Class<? extends Annotation> getAnnotationType() {
		return LongValue.class;
	}

	@Override
	public String convertToString() {
		return String.valueOf(this.fieldValue);
	}

	@Override
	public Long getFieldValue() {
		return this.fieldValue;
	}

	@Override
	public int fractionalLength() {
		throw new UnsupportedOperationException("@LongProperty not support ignoreLimit() method.");
	}

	@Override
	public void setFieldValueFromString(String value) {
		this.fieldValue = Long.parseLong(value);
	}

	@Override
	public void setFieldValueFromObject() {
		try {
			Object value = field.get(this.obj);
			this.fieldValue = value == null ? 0L : (long) value;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("Error: Unable to get field value.", e);
		} catch (ClassCastException e) {
			throw new UnSupportedTypeException(String.format("%s does not support %s type.",
					this.getClass().getSimpleName(), field.getType().getSimpleName()), e);
		}
	}

	@Override
	public void setFieldValueFromBytes(byte[] bytes) {

		long value = 0;

		for (byte b : bytes) {
			int asciiValue = Character.getNumericValue(b);
			value = (value * 10) + asciiValue;
		}

		this.fieldValue = value;
	}
}
