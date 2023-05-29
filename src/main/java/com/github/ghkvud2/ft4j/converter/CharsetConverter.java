package com.github.ghkvud2.ft4j.converter;

import java.nio.charset.Charset;

public interface CharsetConverter {

	Charset getCharset();
	
	byte[] toByteArray(String input, int limit);
	
	String convert(byte[] bytes);
	
	byte[] convert(String input);
}
