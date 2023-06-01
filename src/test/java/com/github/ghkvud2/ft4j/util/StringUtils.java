package com.github.ghkvud2.ft4j.util;

import java.nio.charset.Charset;

import com.github.ghkvud2.ft4j.constant.ConverterType;

public class StringUtils {

	public static String convert(byte[] bytes, ConverterType type) {
		
		if(bytes == null) {
			return "";
		}

		if (type == ConverterType.EUC_KR) {
			return new String(bytes, Charset.forName("EUC-KR"));
		}

		if (type == ConverterType.UTF_8) {
			return new String(bytes, Charset.forName("UTF-8"));
		}

		throw new IllegalArgumentException(String.format("%s illegal argument", type));
	}
}
