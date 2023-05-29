package com.github.ghkvud2.ft4j.property.bytes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.github.ghkvud2.ft4j.annotation.ShortValue;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.exception.UnSupportedTypeException;
import com.github.ghkvud2.ft4j.property.AbstractProperty;

public class ShortProperty extends AbstractProperty<Short> implements ByteProperty {

	public ShortProperty(Object obj, Field field) {
		super(obj, field);
	}

	public ShortProperty(Field field) {
		super(field);
	}

	@Override
	public int length() {
		return ((ShortValue) annotation).length();
	}

	@Override
	public String defaultValue() {
		return ((ShortValue) annotation).defaultValue();
	}

	@Override
	public PaddingByte padding() {
		return ((ShortValue) annotation).paddingByte();
	}

	@Override
	public Justify justify() {
		return ((ShortValue) annotation).justify();
	}

	@Override
	public boolean ignoreLimit() {
		return ((ShortValue) annotation).ignoreLimit();
	}

	@Override
	protected Class<? extends Annotation> getAnnotationType() {
		return ShortValue.class;
	}

	@Override
	public String convertToString() {
		return String.valueOf(this.fieldValue);
	}

	@Override
	public Short getFieldValue() {
		return this.fieldValue;
	}

	@Override
	public int fractionalLength() {
		throw new UnsupportedOperationException("@ShortProperty not support fractionalLength() method.");
	}

	@Override
	public void setFieldValueFromString(String value) {
		this.fieldValue = Short.parseShort(value);
	}

	@Override
	public void setFieldValueFromObject() {
		try {
			Object value = field.get(this.obj);
			this.fieldValue = value == null ? 0 : (short) value;
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

		this.fieldValue = (short) value;
	}

}
