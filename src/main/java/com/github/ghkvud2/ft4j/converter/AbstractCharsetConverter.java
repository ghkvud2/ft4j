package com.github.ghkvud2.ft4j.converter;

public abstract class AbstractCharsetConverter implements CharsetConverter {

	@Override
	public String convert(byte[] input) {
		return new String(input, getCharset());
	}

	@Override
	public byte[] convert(String input) {
		return input.getBytes(getCharset());
	}

}
