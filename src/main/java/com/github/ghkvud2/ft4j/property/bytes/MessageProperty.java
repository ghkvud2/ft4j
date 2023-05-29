package com.github.ghkvud2.ft4j.property.bytes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.github.ghkvud2.ft4j.annotation.Message;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.exception.UnSupportedTypeException;
import com.github.ghkvud2.ft4j.property.AbstractProperty;

public class MessageProperty extends AbstractProperty<String> implements ByteProperty {

	public MessageProperty(Object obj, Field field) {
		super(obj, field);
	}

	public MessageProperty(Field field) {
		super(field);
	}

	@Override
	public int length() {
		return ((Message) annotation).length();
	}

	@Override
	public String defaultValue() {
		return ((Message) annotation).defaultValue();
	}

	@Override
	public PaddingByte padding() {
		return ((Message) annotation).paddingByte();
	}

	@Override
	public Justify justify() {
		return ((Message) annotation).justify();
	}

	@Override
	public boolean ignoreLimit() {
		throw new UnsupportedOperationException("@MessageProperty not support ignoreLimit() method.");
	}

	@Override
	protected Class<? extends Annotation> getAnnotationType() {
		return Message.class;
	}

	@Override
	public String convertToString() {
		return this.fieldValue;
	}

	@Override
	public String getFieldValue() {
		return this.fieldValue;
	}

	@Override
	public int fractionalLength() {
		throw new UnsupportedOperationException("@MessageProperty not support fractionalLength() method.");
	}

	@Override
	public void setFieldValueFromString(String value) {
		this.fieldValue = value;
	}

	@Override
	public void setFieldValueFromObject() {
		try {
			Object value = field.get(this.obj);
			this.fieldValue = value == null ? "" : (String) value;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("Error: Unable to get field value.", e);
		} catch (ClassCastException e) {
			throw new UnSupportedTypeException(String.format("%s does not support %s type.",
					this.getClass().getSimpleName(), field.getType().getSimpleName()), e);
		}
	}

	@Override
	public void setFieldValueFromBytes(byte[] bytes) {
	}
}
