package com.github.ghkvud2.ft4j.validator.bytes;

import com.github.ghkvud2.ft4j.property.AnnotationFieldProperty;
import com.github.ghkvud2.ft4j.validator.PropertyValidator;

public interface BytePropertyValidator extends PropertyValidator {

	boolean isValidDefaultValueLimit(AnnotationFieldProperty property, byte[] defaultValue);

	boolean isValidGeneratedValueLimit(AnnotationFieldProperty property, byte[] generatedValue);
	
	boolean isValidFieldValueLimit(AnnotationFieldProperty property, byte[] fieldValue);
}
