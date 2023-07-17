package com.github.ghkvud2.ft4j.marshall;

import static com.github.ghkvud2.ft4j.util.StringUtils.convert;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.github.ghkvud2.ft4j.annotation.FloatValue;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.exception.DefaultValueExceedsLimitException;
import com.github.ghkvud2.ft4j.exception.FieldValueExceedsLimitException;

@DisplayName("Marshall - @FloatValue 테스트")
public class FloatValueTest {

	private MarshallManager marshaller;
	private ConverterType type;

	@BeforeEach
	void setUp() {
		type = ConverterType.EUC_KR;
		marshaller = MarshallFactory.builder().converter(type).build();
	}

	@DisplayName("length 속성")
	@Nested
	class LengthTest {

		@DisplayName("field length <= length property")
		@ParameterizedTest(name = "when length=10, input={0}, expected={1}, 길이=10")
		@CsvSource(value = { "123.373, 000123.373", "123.999, 000123.999", "123, 0000000123", "1, 0000000001",
				"12345.67, 0012345.67", "12345.9, 00012345.9" })
		void field_equal_or_less__than_length(float input, String expected) {
			Bank bank = new Bank(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("field length > length property and ignoreLimit=false, exception")
		@ParameterizedTest(name = "when length=5, input={0}")
		@CsvSource(value = { "12.345" })
		void field_grater_than_length(float input) {
			Bank4 bank = new Bank4(input);
			assertThrows(FieldValueExceedsLimitException.class, () -> marshaller.marshall(bank));
		}

		@DisplayName("field length > length property and ignoreLimit=true")
		@ParameterizedTest(name = "when length=7, input={0} then expected={1}")
		@CsvSource(value = { "12345.67890, 12345.6", "12.34567, 12.3456" })
		void ignore_limit(float input, String expected) {
			Bank3 bank = new Bank3(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		class Bank {

			@FloatValue(order = 1, length = 10)
			private float rate;

			public Bank(float rate) {
				this.rate = rate;
			}
		}

		class Bank2 {

			@FloatValue(order = 1, length = 7)
			private float rate;

			public Bank2(float rate) {
				this.rate = rate;
			}
		}

		class Bank3 {

			@FloatValue(order = 1, length = 7, ignoreLimit = true)
			private float rate;

			public Bank3(float rate) {
				this.rate = rate;
			}
		}

		class Bank4 {

			@FloatValue(order = 1, length = 5)
			private float rate;

			public Bank4(float rate) {
				this.rate = rate;
			}
		}

	}

	@DisplayName("fractionalLength 속성")
	@Nested
	class FractionalTest {

		@DisplayName("input의 소수점 자릿수 < fractionLength ")
		@ParameterizedTest(name = "input={0}, expected={1}, 소수점=3, 길이=10")
		@CsvSource(value = { "123.4, 000123.400", "1.2, 000001.200", "12.3, 000012.300", "12.34, 000012.340" })
		void fractional_length_test(float input, String expected) {
			Bank bank = new Bank(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("input의 소수점 자릿수 = fractionLength ")
		@ParameterizedTest(name = "input={0}, expected={1}, 소수점=3, 길이=10")
		@CsvSource(value = { "1.234, 000001.234", "12.345, 000012.345", "123.456, 000123.456" })
		void fractional_length_eqauls_test(float input, String expected) {
			Bank bank = new Bank(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("input의 소수점 자릿수 > fractionLength ")
		@ParameterizedTest(name = "input={0}, expected={1}, 소수점=3, 길이=10")
		@CsvSource(value = { "1.2345, 000001.234", "12.3456, 000012.345", "123.4567, 000123.456" })
		void fractional_length_grathar_test(float input, String expected) {
			Bank bank = new Bank(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("fractionLength가 0일 때")
		@ParameterizedTest(name = "input={0}, expected={1}, 소수점=0, 길이=7")
		@CsvSource(value = { "1234.39f, 0001234", "1234.93f, 0001234", "1f, 0000001" })
		void fractional_is_zero_test(float input, String expected) {
			Bank2 bank = new Bank2(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		class Bank {

			@FloatValue(order = 1, length = 10, fractionalLength = 3)
			private float rate;

			public Bank(float rate) {
				this.rate = rate;
			}
		}

		class Bank2 {

			@FloatValue(order = 1, length = 7, fractionalLength = 0)
			private float rate;

			public Bank2(float rate) {
				this.rate = rate;
			}
		}

		class Bank3 {

			@FloatValue(order = 1, length = 7, fractionalLength = -1)
			private float rate;

			public Bank3(float rate) {
				this.rate = rate;
			}
		}
	}

	@DisplayName("defaultValue 속성")
	@Nested
	class DefaultValueTest {

		@DisplayName("defaultValue length < length")
		@ParameterizedTest(name = "input={0}, expected={1}, defaultValue='2.933', length=10")
		@CsvSource(value = { "1.1, 000002.933" })
		void defaultValue_less_than_length(float input, String expected) {
			Bank bank = new Bank(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("defaultValue length = length")
		@ParameterizedTest(name = "input={0}, expected={1}, defaultValue='2.933', length=5")
		@CsvSource(value = { "1.1, 2.933", "9.999, 2.933" })
		void defaultValue_equals_length(float input, String expected) {
			Bank2 bank = new Bank2(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("when defaultValue length > length, then exception")
		@Test
		void defaultValue_exceeds_length() {
			Bank3 bank = new Bank3(1.1f);
			assertThrows(DefaultValueExceedsLimitException.class, () -> marshaller.marshall(bank));
		}

		@DisplayName("when defaultValue is character, then exception")
		@Test
		void defaultValue_not_a_number() {
			Bank4 bank = new Bank4(1.1f);
			assertThrows(NumberFormatException.class, () -> marshaller.marshall(bank));
		}

		class Bank {

			@FloatValue(order = 1, length = 10, defaultValue = "2.933")
			private float rate;

			public Bank(float rate) {
				this.rate = rate;
			}
		}

		class Bank2 {

			@FloatValue(order = 1, length = 5, defaultValue = "2.933")
			private float rate;

			public Bank2(float rate) {
				this.rate = rate;
			}
		}

		class Bank3 {

			@FloatValue(order = 1, length = 3, defaultValue = "2.933")
			private float rate;

			public Bank3(float rate) {
				this.rate = rate;
			}
		}

		class Bank4 {

			@FloatValue(order = 1, length = 3, defaultValue = "A")
			private float rate;

			public Bank4(float rate) {
				this.rate = rate;
			}
		}
	}

	@DisplayName("paddingByte 속성")
	@Nested
	class PaddingByteTest {

		@DisplayName("default padding byte")
		@ParameterizedTest(name = "when length=10, input={0} then expected={1}")
		@CsvSource(value = { "1.2, 00000001.2", "12.3, 00000012.3", "123.4, 00000123.4", "1234.5, 00001234.5" })
		void default_padding(float input, String expected) {
			Bank bank = new Bank(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("space padding byte")
		@ParameterizedTest(name = "when length=10, input={0} then expected={1}")
		@CsvSource(value = { "1.2, '       1.2'", "12.3, '      12.3'", "123.4, '     123.4'", "1234.5, '    1234.5'" })
		void zero_padding(float input, String expected) {
			Bank3 bank = new Bank3(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("when double field length = length, then padding byte no effects")
		@ParameterizedTest(name = "when length=5 then expected={1}")
		@CsvSource(value = { "12.34, 12.34", "1.999, 1.999" })
		void double_field_length_equals_to_length(float input, String expected) {
			Bank4 bank = new Bank4(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		class Bank {

			@FloatValue(order = 1, length = 10)
			private float rate;

			public Bank(float rate) {
				this.rate = rate;
			}
		}

		class Bank2 {

			@FloatValue(order = 1, length = 5, paddingByte = PaddingByte.SPACE)
			private float rate;

			public Bank2(float rate) {
				this.rate = rate;
			}
		}

		class Bank3 {

			@FloatValue(order = 1, length = 10, paddingByte = PaddingByte.SPACE)
			private float rate;

			public Bank3(float rate) {
				this.rate = rate;
			}
		}

		class Bank4 {

			@FloatValue(order = 1, length = 5, paddingByte = PaddingByte.SPACE)
			private float rate;

			public Bank4(float rate) {
				this.rate = rate;
			}
		}

	}

	@Nested
	@DisplayName("justify 속성")
	class JustifyTest {

		@DisplayName("RIGHT")
		@ParameterizedTest(name = "when length=10, input={0} then expected={1}")
		@CsvSource(value = { "12.3, 00000012.3", "12.99, 0000012.99" })
		void right_justify_test(float input, String expected) {
			Bank bank = new Bank(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("LEFT")
		@ParameterizedTest(name = "when length=10, input={0} then expected={1}")
		@CsvSource(value = { "12.3, 12.3000000", "12.99, 12.9900000" })
		void left_justify_test(float input, String expected) {
			Bank2 bank = new Bank2(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		class Bank {

			@FloatValue(order = 1, length = 10)
			private float rate;

			public Bank(float rate) {
				this.rate = rate;
			}
		}

		class Bank2 {

			@FloatValue(order = 1, length = 10, justify = Justify.LEFT)
			private float rate;

			public Bank2(float rate) {
				this.rate = rate;
			}
		}
	}
}
