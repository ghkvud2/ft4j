package com.github.ghkvud2.ft4j.marshall;

import static com.github.ghkvud2.ft4j.util.StringUtils.convert;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.github.ghkvud2.ft4j.annotation.DoubleValue;
import com.github.ghkvud2.ft4j.annotation.FloatValue;
import com.github.ghkvud2.ft4j.annotation.Order;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.exception.DefaultValueExceedsLimitException;
import com.github.ghkvud2.ft4j.exception.FieldValueExceedsLimitException;
import com.github.ghkvud2.ft4j.marshall.MarshallFactory;
import com.github.ghkvud2.ft4j.marshall.MarshallManager;

@DisplayName("Marshall - @Float, Double 어노테이션")
public class DecimalTest {

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
				"1234567.89, 1234567.89", "12345678.9, 12345678.9" })
		void field_equal_or_less__than_length(double input, String expected) {
			Bank bank = new Bank(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("field length > length property and ignoreLimit=false, exception")
		@ParameterizedTest(name = "when length=10, input={0}")
		@CsvSource(value = { "12345.67892", "12345.67891" })
		void field_grater_than_length(double input) {
			Bank bank = new Bank(input);
			assertThrows(FieldValueExceedsLimitException.class, () -> marshaller.marshall(bank));
		}

		@DisplayName("field length > length property and ignoreLimit=true")
		@ParameterizedTest(name = "when length=7, input={0} then expected={1}")
		@CsvSource(value = { "12345.67890, 12345.6", "12.34567, 12.3456" })
		void ignore_limit(double input, String expected) {
			Bank3 bank = new Bank3(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		class Bank {

			@Order(1)
			@DoubleValue(length = 10)
			private double rate;

			public Bank(double rate) {
				this.rate = rate;
			}
		}

		class Bank2 {
			
			@Order(1)
			@FloatValue(length = 7)
			private float rate;

			public Bank2(float rate) {
				this.rate = rate;
			}
		}

		class Bank3 {

			@Order(1)
			@DoubleValue(length = 7, ignoreLimit = true)
			private double rate;

			public Bank3(double rate) {
				this.rate = rate;
			}
		}

	}

	@DisplayName("fractionalLength 속성")
	@Nested
	class FractionalTest {

		@DisplayName("input의 소수점 자릿수 < fractionLength ")
		@ParameterizedTest(name = "input={0}, expected={1}, 소수점=3, 길이=10")
		@CsvSource(value = { "12345.67, 012345.670", "123456.78, 123456.780", "1.60, 000001.600" })
		void fractional_length_test(double input, String expected) {
			Bank bank = new Bank(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("input의 소수점 자릿수 = fractionLength ")
		@ParameterizedTest(name = "input={0}, expected={1}, 소수점=3, 길이=10")
		@CsvSource(value = { "12345.392, 012345.392", "123456.934, 123456.934", "1.62, 000001.620" })
		void fractional_length_eqauls_test(double input, String expected) {
			Bank bank = new Bank(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("input의 소수점 자릿수 > fractionLength ")
		@ParameterizedTest(name = "input={0}, expected={1}, 소수점=3, 길이=10")
		@CsvSource(value = { "1234.3924, 001234.392", "12345.9349, 012345.934", "1.629999, 000001.629" })
		void fractional_length_grathar_test(double input, String expected) {
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
			
			@Order(1)
			@DoubleValue(length = 10, fractionalLength = 3)
			private double rate;

			public Bank(double rate) {
				this.rate = rate;
			}
		}

		class Bank2 {

			@Order(1)
			@FloatValue(length = 7, fractionalLength = 0)
			private float rate;

			public Bank2(float rate) {
				this.rate = rate;
			}
		}

		class Bank3 {

			@Order(1)
			@DoubleValue(length = 7, fractionalLength = -1)
			private double rate;

			public Bank3(double rate) {
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
		void defaultValue_less_than_length(double input, String expected) {
			Bank bank = new Bank(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("defaultValue length = length")
		@ParameterizedTest(name = "input={0}, expected={1}, defaultValue='2.933', length=5")
		@CsvSource(value = { "1.1, 2.933" })
		void defaultValue_equals_length(double input, String expected) {
			Bank2 bank = new Bank2(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("when defaultValue length > length, then exception")
		@Test
		void defaultValue_exceeds_length() {
			Bank3 bank = new Bank3(1.1);
			assertThrows(DefaultValueExceedsLimitException.class, () -> marshaller.marshall(bank));
		}

		@DisplayName("when defaultValue is character, then exception")
		@Test
		void defaultValue_not_a_number() {
			Bank4 bank = new Bank4(1.1);
			assertThrows(NumberFormatException.class, () -> marshaller.marshall(bank));
		}

		class Bank {

			@Order(1)
			@DoubleValue(length = 10, defaultValue = "2.933")
			private double rate;

			public Bank(double rate) {
				this.rate = rate;
			}
		}

		class Bank2 {

			@Order(1)
			@DoubleValue(length = 5, defaultValue = "2.933")
			private double rate;

			public Bank2(double rate) {
				this.rate = rate;
			}
		}

		class Bank3 {

			@Order(1)
			@DoubleValue(length = 3, defaultValue = "2.933")
			private double rate;

			public Bank3(double rate) {
				this.rate = rate;
			}
		}

		class Bank4 {

			@Order(1)
			@DoubleValue(length = 3, defaultValue = "A")
			private double rate;

			public Bank4(double rate) {
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
		void zero_padding(double input, String expected) {
			Bank3 bank = new Bank3(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("when float field length = length, then padding byte no effects")
		@ParameterizedTest(name = "when length=5 then expected={1}")
		@CsvSource(value = { "12.34, 12.34", "1.234, 1.234" })
		void float_field_length_equals_to_length(float input, String expected) {
			Bank2 bank = new Bank2(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("when double field length = length, then padding byte no effects")
		@ParameterizedTest(name = "when length=10 then expected={1}")
		@CsvSource(value = { "12345678.9, 12345678.9" })
		void double_field_length_equals_to_length(double input, String expected) {
			Bank3 bank = new Bank3(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		class Bank {

			@Order(1)
			@FloatValue(length = 10)
			private float rate;

			public Bank(float rate) {
				this.rate = rate;
			}
		}

		class Bank2 {

			@Order(1)
			@FloatValue(length = 5, paddingByte = PaddingByte.SPACE)
			private float rate;

			public Bank2(float rate) {
				this.rate = rate;
			}
		}

		class Bank3 {

			@Order(1)
			@DoubleValue(length = 10, paddingByte = PaddingByte.SPACE)
			private double rate;

			public Bank3(double rate) {
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
		void right_justify_test(double input, String expected) {
			Bank bank = new Bank(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("LEFT")
		@ParameterizedTest(name = "when length=10, input={0} then expected={1}")
		@CsvSource(value = { "12.3, 12.3000000", "12.99, 12.9900000" })
		void left_justify_test(double input, String expected) {
			Bank2 bank = new Bank2(input);
			byte[] result = marshaller.marshall(bank);
			assertEquals(expected, convert(result, type));
		}

		class Bank {

			@Order(1)
			@DoubleValue(length = 10)
			private double rate;

			public Bank(double rate) {
				this.rate = rate;
			}
		}

		class Bank2 {

			@Order(1)
			@DoubleValue(length = 10, justify = Justify.LEFT)
			private double rate;

			public Bank2(double rate) {
				this.rate = rate;
			}
		}
	}
}
