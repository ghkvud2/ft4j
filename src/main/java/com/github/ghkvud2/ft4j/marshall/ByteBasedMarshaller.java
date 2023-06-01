package com.github.ghkvud2.ft4j.marshall;

import java.util.Arrays;

import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.converter.CharsetConverter;
import com.github.ghkvud2.ft4j.property.AnnotationFieldProperty;
import com.github.ghkvud2.ft4j.property.bytes.ByteProperty;

public class ByteBasedMarshaller implements Marshaller {

	private final CharsetConverter converter;

	public ByteBasedMarshaller(CharsetConverter converter) {
		this.converter = converter;
	}

	@Override
	public boolean supports(AnnotationFieldProperty property) {
		return property instanceof ByteProperty;
	}

	@Override
	public byte[] marshall(AnnotationFieldProperty property) {

		int limit = property.length();
		PaddingByte paddingByte = property.padding();
		Justify justify = property.justify();

		String input = property.convertToString();
		byte[] originBytes = converter.toByteArray(input, limit);
		int originLen = originBytes.length;
		byte[] newBytes = new byte[limit];

		int start = 0;
		int end = limit;

		if (justify == Justify.LEFT) {
			System.arraycopy(originBytes, 0, newBytes, 0, originLen);
			start = originLen;
		} else if (justify == Justify.RIGHT) {
			System.arraycopy(originBytes, 0, newBytes, limit - originLen, originLen);
			end = limit - originLen;
		}

		if (originLen < limit) {
			Arrays.fill(newBytes, start, end, paddingByte.getValue());
		}
		return newBytes;
//		return new String(newBytes, converter.getCharset());
	}

	@Override
	public String convertWithCharset(String input) {
		byte[] bytes = input.getBytes(converter.getCharset());
		return new String(bytes, converter.getCharset());
	}
}
