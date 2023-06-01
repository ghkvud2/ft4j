package com.github.ghkvud2.ft4j.validator;

import java.util.List;

import com.github.ghkvud2.ft4j.converter.CharsetConverter;
import com.github.ghkvud2.ft4j.exception.*;
import com.github.ghkvud2.ft4j.property.AnnotationFieldProperty;
import com.github.ghkvud2.ft4j.validator.bytes.BytePropertyValidator;

public class ByteValidatorManager implements ValidatorManager {

	private final CharsetConverter converter;
	private final List<PropertyValidator> validators;

	public ByteValidatorManager(CharsetConverter converter, List<PropertyValidator> validators) {
		this.converter = converter;
		this.validators = validators;
	}

	@Override
	public void preValidate(AnnotationFieldProperty property) {

		String fieldValue = property.convertToString();
		byte[] bytesBeforeMarshalling = fieldValue.getBytes(converter.getCharset());

		boolean isSupported = false;

		for (PropertyValidator validator : validators) {

			if (!validator.support(property)) {
				continue;
			}

			isSupported = true;
			if (!validator.isValidType(property)) {
				throw new UnSupportedTypeException(String.format("Type %s of Field '%s' is unsupported",
						property.getField().getType().getSimpleName(), property.getField().getName()));
			}

			if (!((BytePropertyValidator) validator).isValidGeneratedValueLimit(property, bytesBeforeMarshalling)) {
				throw new GeneratedValueExceedsLimitException(String.format(
						"Invalid generated value is %s: The length of the generated value exceeds the specified length limit %d. Please check Generator which provide the value.",
						property.convertToString(), property.length()));
			}

			if (!((BytePropertyValidator) validator).isValidDefaultValueLimit(property, bytesBeforeMarshalling)) {
				throw new DefaultValueExceedsLimitException(String.format(
						"Invalid default value is %s: The length of the default value exceeds the specified length limit %d. Please provide a defaultValue within the allowed length.",
						property.convertToString(), property.length()));
			}

			if (!((BytePropertyValidator) validator).isValidFieldValueLimit(property, bytesBeforeMarshalling)) {
				throw new FieldValueExceedsLimitException(
						String.format("The value '%s' of field %s exceeds limit length %d.", fieldValue.toString(),
								property.getField().getName(), property.length()));
			}

		}
		if (!isSupported) {
			throw new UnsupportedOperationException("No validator supports the given property");
		}
	}

	@Override
	public void postValidate(AnnotationFieldProperty property, byte[] bytes) {

		boolean isSupported = false;
		for (PropertyValidator validator : validators) {
			if (!validator.support(property)) {
				continue;
			}
			isSupported = true;
			int expectedLength = property.length();
			int actualLength = bytes.length;

			if (expectedLength != actualLength) {
				throw new LengthMismatchException(String.format("Field %s mismatch length. actual length is %d.",
						property.getField().getName(), actualLength));
			}
		}
		if (!isSupported) {
			throw new UnsupportedOperationException("No validator supports the given property");
		}
	}

}
