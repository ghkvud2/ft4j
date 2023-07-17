package com.github.ghkvud2.ft4j.validator.bytes;

import com.github.ghkvud2.ft4j.property.AnnotationFieldProperty;
import com.github.ghkvud2.ft4j.property.bytes.StringProperty;

public class ByteStringPropertyValidator implements BytePropertyValidator {

	@Override
	public boolean support(AnnotationFieldProperty property) {
		return property.getClass().isAssignableFrom(StringProperty.class);
	}

	@Override
	public boolean isValidType(AnnotationFieldProperty property) {

		Class<?> fieldType = property.getField().getType();

		if (fieldType.equals(String.class)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isValidDefaultValueLimit(AnnotationFieldProperty property, byte[] defaultValue) {

		if (property.isUseDefaultValue() && defaultValue.length > property.length()) {
			return false;
		}

		return true;
	}

	@Override
	public boolean isValidGeneratedValueLimit(AnnotationFieldProperty property, byte[] generatedValue) {

		if (property.isUseGenerator() && generatedValue.length > property.length()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isValidFieldValueLimit(AnnotationFieldProperty property, byte[] fieldValue) {
		return true;
	}

}
