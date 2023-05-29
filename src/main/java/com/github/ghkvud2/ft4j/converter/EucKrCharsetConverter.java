package com.github.ghkvud2.ft4j.converter;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class EucKrCharsetConverter extends AbstractCharsetConverter {

	private final Charset charset;

	public EucKrCharsetConverter() {
		this.charset = Charset.forName("EUC-KR");
	}

	@Override
	public Charset getCharset() {
		return this.charset;
	}

	@Override
	public byte[] toByteArray(String input, int limit) {
		CharsetEncoder charsetEncoder = charset.newEncoder();

		int byteCount = 0;
		int charCount = 0;
		StringBuilder truncatedString = new StringBuilder();

		while (charCount < input.length()) {
			char c = input.charAt(charCount);

			if (charsetEncoder.canEncode(c)) {
				int charBytes = c <= 0x7F ? 1 : 2;

				if (byteCount + charBytes > limit) {
					break;
				}

				byteCount += charBytes;
				truncatedString.append(c);
			}
			charCount++;
		}
		byte[] bytes = truncatedString.toString().getBytes(charset);
		return bytes;
	}

}
