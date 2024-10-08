package com.github.ghkvud2.ft4j.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("MS949 인코딩")
class Ms949CharsetConverterTest {

	private CharsetConverter converter;

	@BeforeEach
	void setUp() {
		converter = new MS949CharsetConverter();
	}
	
	@DisplayName("MS949 한글 인코딩 테스트")
	@ParameterizedTest
	@MethodSource("provider")
	void encode_ms949(int limit, String input, String expected) {
		byte[] bytes = converter.toByteArray(input, limit);
		String result = new String(bytes, converter.getCharset());
		assertEquals(expected, result);
	}
	
	static Stream<Arguments> provider(){

		String input = "1간2다a@#!쀏셺나라ab";
		
		return Stream.of(
				Arguments.of(1, input, "1"),
				Arguments.of(2, input, "1"),
				Arguments.of(3, input, "1간"),
				Arguments.of(4, input, "1간2"),
				Arguments.of(5, input, "1간2"),
				Arguments.of(6, input, "1간2다"),
				Arguments.of(7, input, "1간2다a"),
				Arguments.of(8, input, "1간2다a@"),
				Arguments.of(9, input, "1간2다a@#"),
				Arguments.of(10,input,  "1간2다a@#!"),
				Arguments.of(11,input,  "1간2다a@#!"),
				Arguments.of(12,input,  "1간2다a@#!쀏"),
				Arguments.of(13,input,  "1간2다a@#!쀏"),
				Arguments.of(14,input,  "1간2다a@#!쀏셺"),
				Arguments.of(15,input,  "1간2다a@#!쀏셺"),
				Arguments.of(16,input,  "1간2다a@#!쀏셺나"),
				Arguments.of(17,input,  "1간2다a@#!쀏셺나"),
				Arguments.of(18,input,  "1간2다a@#!쀏셺나라"),
				Arguments.of(19,input,  "1간2다a@#!쀏셺나라a"),
				Arguments.of(20,input,  "1간2다a@#!쀏셺나라ab")
				); 
	
	}

}
