package com.github.ghkvud2.ft4j.marshall;

import static com.github.ghkvud2.ft4j.util.StringUtils.convert;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.github.ghkvud2.ft4j.annotation.IntValue;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.exception.DefaultValueExceedsLimitException;
import com.github.ghkvud2.ft4j.exception.FieldValueExceedsLimitException;

@DisplayName("Marshall - @IntValue 테스트")
public class IntValueTest {

	private MarshallManager marshaller;
	private ConverterType type;

	@BeforeEach
	void setUp() {
		type = ConverterType.UTF_8;
		marshaller = MarshallFactory.builder().converter(type).build();
	}

	@DisplayName("length 속성")
	@Nested
	class LengthTest {

		@DisplayName("field length <= length property")
		@ParameterizedTest(name = "when length=5, input={0} then expected={1}")
		@CsvSource(value = { "1, 00001", "12, 00012", "123, 00123", "1234, 01234", "12345, 12345" })
		void field_equal_or_less__than_length(int price, String expected) {
			Product product = new Product(price);
			byte[] result = marshaller.marshall(product);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("field length > length property and ignoreLimit=false, exception")
		@Test
		void field_grater_than_length() {
			Product2 product = new Product2(1234);
			assertThrows(FieldValueExceedsLimitException.class, () -> marshaller.marshall(product));
		}

		@DisplayName("field length > length property and ignoreLimit=true")
		@ParameterizedTest(name = "when length=3, input={0} then expected={1}")
		@CsvSource(value = { "1234, 123", "12345, 123" })
		void ignore_limit(int input, String expected) {
			Product3 product = new Product3(input);
			byte[] result = marshaller.marshall(product);
			assertEquals(expected, convert(result, type));
		}

		class Product {

			@IntValue(order = 1, length = 5)
			private int price;

			public Product(int price) {
				this.price = price;
			}
		}

		class Product2 {

			@IntValue(order = 1, length = 3)
			private int price;

			public Product2(int price) {
				this.price = price;
			}
		}

		class Product3 {

			@IntValue(order = 1, length = 3, ignoreLimit = true)
			private int price;

			public Product3(int price) {
				this.price = price;
			}
		}

	}

	@DisplayName("defaultValue 속성")
	@Nested
	class DefaultValueTest {

		@DisplayName("default value length < length property")
		@Test
		void defaultValue_less_than_length() {
			Product product = new Product(1);
			byte[] result = marshaller.marshall(product);
			assertEquals("00099", convert(result, type));
		}

		@DisplayName("default value length > length property then exception")
		@Test
		void defaultValue_exceed_length() {
			Product2 product = new Product2(1);
			assertThrows(DefaultValueExceedsLimitException.class, () -> marshaller.marshall(product));
		}

		@DisplayName("default value length = length property")
		void defaultValue_eqauls_to_length(String expected) {
			Product3 product = new Product3(1);
			byte[] result = marshaller.marshall(product);
			assertEquals("12345", convert(result, type));
		}

		@DisplayName("default value can't convert to number type")
		@Test
		void defaultValue_number_format_exception() {
			Product4 product = new Product4(1);
			assertThrows(NumberFormatException.class, () -> marshaller.marshall(product));
		}

		class Product {

			@IntValue(order = 1, length = 5, defaultValue = "99")
			private int price;

			public Product(int price) {
				this.price = price;
			}
		}

		class Product2 {

			@IntValue(order = 1, length = 3, defaultValue = "1234")
			private int price;

			public Product2(int price) {
				this.price = price;
			}
		}

		class Product3 {

			@IntValue(order = 1, length = 5, defaultValue = "12345")
			private int price;

			public Product3(int price) {
				this.price = price;
			}
		}

		class Product4 {

			@IntValue(order = 1, length = 5, defaultValue = "ABC")
			private int price;

			public Product4(int price) {
				this.price = price;
			}
		}
	}

	@DisplayName("paddingByte 속성")
	@Nested
	class PaddingByteTest {

		@DisplayName("default padding byte")
		@ParameterizedTest(name = "when length=5 then expected={1}")
		@CsvSource(value = { "1, 00001", "12, 00012", "123, 00123", "1234, 01234", "12345, 12345" })
		void default_padding(int price, String expected) {
			Product product = new Product(price);
			byte[] result = marshaller.marshall(product);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("zero padding byte")
		@ParameterizedTest(name = "when length=5 then expected={1}")
		@CsvSource(value = { "1,'    1'", "12,'   12'", "123,'  123'", "1234, ' 1234'", "12345, 12345" })
		void zero_padding(int price, String expected) {
			Product2 product = new Product2(price);
			byte[] result = marshaller.marshall(product);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("when field length = length, then padding byte no effects")
		@ParameterizedTest(name = "when length=5 then expected={1}")
		@CsvSource(value = { "12345, 12345" })
		void field_length_equals_to_length(int price, String expected) {
			Product2 product = new Product2(price);
			byte[] result = marshaller.marshall(product);
			assertEquals(expected, convert(result, type));
		}

		class Product {

			@IntValue(order = 1, length = 5)
			private int price;

			public Product(int price) {
				this.price = price;
			}
		}

		class Product2 {

			@IntValue(order = 1, length = 5, paddingByte = PaddingByte.SPACE)
			private int price;

			public Product2(int price) {
				this.price = price;
			}
		}
	}

	@Nested
	@DisplayName("justify 속성")
	class JustifyTest {

		@DisplayName("RIGHT")
		@ParameterizedTest(name = "when length=5, input={0} then expected={1}")
		@CsvSource(value = { "1, 00001", "12345, 12345" })
		void right_justify_test(int input, String expected) {
			Product product = new Product(input);
			byte[] result = marshaller.marshall(product);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("LEFT")
		@ParameterizedTest(name = "when length=5, input={0} then expected={1}")
		@CsvSource(value = { "1, 10000", "12345, 12345" })
		void left_justify_test(int input, String expected) {
			Product2 product = new Product2(input);
			byte[] result = marshaller.marshall(product);
			assertEquals(expected, convert(result, type));
		}

		class Product {

			@IntValue(order = 1, length = 5)
			private int price;

			public Product(int price) {
				this.price = price;
			}
		}

		class Product2 {

			@IntValue(order = 1, length = 5, justify = Justify.LEFT)
			private int price;

			public Product2(int price) {
				this.price = price;
			}
		}
	}

}
