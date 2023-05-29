package com.github.ghkvud2.ft4j.property.bytes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.github.ghkvud2.ft4j.annotation.IntValue;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.exception.UnSupportedTypeException;
import com.github.ghkvud2.ft4j.property.AbstractProperty;

public class IntProperty extends AbstractProperty<Integer> implements ByteProperty {

	public IntProperty(Object obj, Field field) {
		super(obj, field);
	}

	public IntProperty(Field field) {
		super(field);
	}

	@Override
	public int length() {
		return ((IntValue) annotation).length();
	}

	@Override
	public String defaultValue() {
		return ((IntValue) annotation).defaultValue();
	}

	@Override
	public PaddingByte padding() {
		return ((IntValue) annotation).paddingByte();
	}

	@Override
	public Justify justify() {
		return ((IntValue) annotation).justify();
	}

	@Override
	public boolean ignoreLimit() {
		return ((IntValue) annotation).ignoreLimit();
	}

	@Override
	protected Class<? extends Annotation> getAnnotationType() {
		return IntValue.class;
	}

	@Override
	public String convertToString() {
		return String.valueOf(this.fieldValue);
	}

	@Override
	public Integer getFieldValue() {
		return this.fieldValue;
	}

	@Override
	public int fractionalLength() {
		throw new UnsupportedOperationException("@IntProperty not support ignoreLimit() method.");
	}

	@Override
	public void setFieldValueFromString(String value) {
		this.fieldValue = Integer.parseInt(value);
	}

	@Override
	public void setFieldValueFromObject() {
		try {
			Object value = field.get(this.obj);
			this.fieldValue = value == null ? 0 : (int) value;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("Error: Unable to get field value.", e);
		} catch (ClassCastException e) {
			throw new UnSupportedTypeException(String.format("%s does not support %s type.",
					this.getClass().getSimpleName(), field.getType().getSimpleName()), e);
		}
	}

	@Override
	public void setFieldValueFromBytes(byte[] bytes) {

		int value = 0;

		for (byte b : bytes) {
			int asciiValue = Character.getNumericValue(b);
			value = (value * 10) + asciiValue;
		}

		this.fieldValue = value;
	}

}
