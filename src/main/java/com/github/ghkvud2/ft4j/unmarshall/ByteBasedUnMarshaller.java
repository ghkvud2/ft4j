package com.github.ghkvud2.ft4j.unmarshall;

import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.converter.CharsetConverter;
import com.github.ghkvud2.ft4j.exception.FieldValueUnderFlowException;
import com.github.ghkvud2.ft4j.property.AnnotationFieldProperty;
import com.github.ghkvud2.ft4j.property.bytes.ByteProperty;

public class ByteBasedUnMarshaller implements UnMarshaller {

	private final CharsetConverter converter;

	public ByteBasedUnMarshaller(CharsetConverter converter) {
		this.converter = converter;
	}

	@Override
	public boolean supports(AnnotationFieldProperty property) {
		return property instanceof ByteProperty;
	}

	@Override
	public int unmarshall(AnnotationFieldProperty property, int offset, byte[] bytes) {

		int limit = property.length();
		int start = offset;
		int end = offset + limit - 1;

		PaddingByte paddingByte = property.padding();
		Justify justify = property.justify();

		if (end >= bytes.length) {
			throw new FieldValueUnderFlowException("During unmarshalling, Field value bytes underflow.");
		}

		if (justify == Justify.LEFT) {
			while (start <= end && paddingByte.getValue() == bytes[end]) {
				end--;
			}
		} else if (justify == Justify.RIGHT) {
			while (start <= end && paddingByte.getValue() == bytes[start]) {
				start++;
			}
		}

		byte[] newBytes = new byte[end - start + 1];
		System.arraycopy(bytes, start, newBytes, 0, newBytes.length);
		newBytes = converter.toByteArray(converter.convert(newBytes), newBytes.length);

		if (property.getField().getType().equals(String.class)) {
			property.setFieldValueFromString(converter.convert(newBytes));
		} else {
			property.setFieldValueFromBytes(newBytes);
		}

		return offset + limit;
	}

	public CharsetConverter getConverter() {
		return converter;
	}

	@Override
	public byte[] convertToBytes(String input) {
		return input.getBytes(converter.getCharset());
	}

}
