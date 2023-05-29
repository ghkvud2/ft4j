package com.github.ghkvud2.ft4j.validator.bytes;

import com.github.ghkvud2.ft4j.property.AnnotationFieldProperty;
import com.github.ghkvud2.ft4j.property.bytes.LongProperty;

public class ByteLongPropertyValidator implements BytePropertyValidator {

	@Override
	public boolean support(AnnotationFieldProperty property) {
		return property.getClass().isAssignableFrom(LongProperty.class);
	}

	@Override
	public boolean isValidType(AnnotationFieldProperty property) {

		Class<?> fieldType = property.getField().getType();

		if (fieldType.equals(long.class)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isValidDefaultValueLimit(AnnotationFieldProperty property, byte[] defaultValue) {

		boolean ignoreLimit = property.ignoreLimit();

		if (property.isUseDefaultValue() && !ignoreLimit && defaultValue.length > property.length()) {
			return false;
		}

		return true;
	}

	@Override
	public boolean isValidGeneratedValueLimit(AnnotationFieldProperty property, byte[] generatedValue) {

		boolean ignoreLimit = property.ignoreLimit();

		if (property.isUseGenerator() && !ignoreLimit && generatedValue.length > property.length()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isValidFieldValueLimit(AnnotationFieldProperty property, byte[] fieldValue) {

		boolean ignoreLimit = property.ignoreLimit();

		if (!property.isUseDefaultValue() && !property.isUseGenerator() && !ignoreLimit
				&& fieldValue.length > property.length()) {
			return false;
		}
		return true;
	}

}
