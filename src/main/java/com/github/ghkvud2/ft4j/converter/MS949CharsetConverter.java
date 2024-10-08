package com.github.ghkvud2.ft4j.converter;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;

public class MS949CharsetConverter extends AbstractCharsetConverter {

	private final Charset charset;

	public MS949CharsetConverter() {
		this.charset = Charset.forName("MS949");
	}

	@Override
	public Charset getCharset() {
		return charset;
	}

	@Override
	public byte[] toByteArray(String input, int limit) {
		CharsetDecoder decoder = charset.newDecoder();
		byte[] inputBytes = input.getBytes(charset);
		int bufferLength = Math.min(inputBytes.length, limit);

		ByteBuffer inputByteBuffer = ByteBuffer.wrap(inputBytes, 0, bufferLength);
		CharBuffer charBuffer = CharBuffer.allocate(bufferLength);

		decoder.onMalformedInput(CodingErrorAction.IGNORE);
		CoderResult result = decoder.decode(inputByteBuffer, charBuffer, true);

		if (result.isOverflow()) {
			decoder.flush(charBuffer);
		}

		charBuffer.flip();
		ByteBuffer truncatedByteBuffer = charset.encode(charBuffer);

		byte[] truncatedBytes = new byte[truncatedByteBuffer.remaining()];
		truncatedByteBuffer.get(truncatedBytes);

		return truncatedBytes;
	}
}
