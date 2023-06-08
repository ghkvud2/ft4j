package com.github.ghkvud2.ft4j.marshall;

import static com.github.ghkvud2.ft4j.util.StringUtils.convert;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.github.ghkvud2.ft4j.annotation.Message;
import com.github.ghkvud2.ft4j.annotation.Order;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.exception.DefaultValueExceedsLimitException;

@DisplayName("Marshall - @Message 어노테이션 UTF-8")
public class Utf8MessageTest {

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
		@ParameterizedTest(name = "when length=10, input={0} then expected={1}")
		@CsvSource(value = { "John, 'John      '", "이화평, '이화평 '", "a@!@#12, 'a@!@#12   '", "AB이CD, 'AB이CD   '",
				"1234567890, 1234567890", "가나다1, 가나다1" })
		void field_equal_or_less__than_length(String input, String expected) {
			User user = new User(input);
			byte[] result = marshaller.marshall(user);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("field length > length property")
		@ParameterizedTest(name = "when length=10, input={0} then expected={1}")
		@CsvSource(value = { "1234567890123, 1234567890", "ABCDEFGHIJKLMN, ABCDEFGHIJ", "가나다라, '가나다 '", "가나다123,가나다1" })
		void field_grater_than_length(String input, String expected) {
			User user = new User(input);
			byte[] result = marshaller.marshall(user);
			assertEquals(expected, convert(result, type));
		}

		class User {
			@Order(1)
			@Message(length = 10)
			private String name;

			public User(String name) {
				this.name = name;
			}
		}
	}

	@DisplayName("defaultValue 속성")
	@Nested
	class DefaultValueTest {

		@DisplayName("default value length < length property")
		@Test
		void defaultValue_less_than_length() {
			User user = new User("test");
			byte[] result = marshaller.marshall(user);
			assertEquals("가나다      ", convert(result, type));
		}

		@DisplayName("default value length > length property then exception")
		@Test
		void defaultValue_exceed_length() {
			User2 user = new User2("exception");
			assertThrows(DefaultValueExceedsLimitException.class, () -> marshaller.marshall(user));
		}

		@DisplayName("default value length = length property")
		@Test
		void defaultValue_eqauls_to_length() {
			User3 user = new User3("test");
			byte[] result = marshaller.marshall(user);
			assertEquals("가나다라마", convert(result, type));
		}

		class User {

			@Order(1)
			@Message(length = 15, defaultValue = "가나다")
			private String name;

			public User(String name) {
				this.name = name;
			}
		}

		class User2 {
			@Order(1)
			@Message(length = 3, defaultValue = "my name")
			private String name;

			public User2(String name) {
				this.name = name;
			}
		}

		class User3 {
			@Order(1)
			@Message(length = 15, defaultValue = "가나다라마")
			private String name;

			public User3(String name) {
				this.name = name;
			}
		}
	}

	@DisplayName("paddingByte 속성")
	@Nested
	class PaddingByteTest {

		@DisplayName("default padding byte")
		@ParameterizedTest(name = "when length=5 then expected={1}")
		@CsvSource(value = { "1, '1    '", "12, '12   '" })
		void default_padding(String input, String expected) {
			User user = new User(input);
			byte[] result = marshaller.marshall(user);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("default padding byte")
		@ParameterizedTest(name = "when length=5 then expected={1}")
		@CsvSource(value = { "1, 10000", "12, 12000" })
		void zero_padding(String input, String expected) {
			User2 user = new User2(input);
			byte[] result = marshaller.marshall(user);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("when field length = length, then padding byte no effects")
		@ParameterizedTest(name = "when length=5 then expected={1}")
		@CsvSource(value = { "12345, 12345" })
		void field_length_equals_to_length(String input, String expected) {
			User user = new User(input);
			byte[] result = marshaller.marshall(user);
			assertEquals(expected, convert(result, type));
		}

		class User {
			@Order(1)
			@Message(length = 5)
			private String name;

			public User(String name) {
				this.name = name;
			}
		}

		class User2 {
			@Order(1)
			@Message(length = 5, paddingByte = PaddingByte.ZERO)
			private String name;

			public User2(String name) {
				this.name = name;
			}
		}
	}

	@Nested
	@DisplayName("justify 속성")
	class JustifyTest {

		@DisplayName("LEFT")
		@ParameterizedTest(name = "when length=10, input={0} then expected={1}")
		@CsvSource(value = { "1, '1         '", "12345, '12345     '" })
		void left_justify_test(String input, String expected) {
			User user = new User(input);
			byte[] result = marshaller.marshall(user);
			assertEquals(expected, convert(result, type));
		}

		@DisplayName("RIGHT")
		@ParameterizedTest(name = "when length=10, input={0} then expected={1}")
		@CsvSource(value = { "1, '         1'", "12345, '     12345'" })
		void right_justify_test(String input, String expected) {
			User2 user = new User2(input);
			byte[] result = marshaller.marshall(user);
			assertEquals(expected, convert(result, type));
		}

		class User {
			@Order(1)
			@Message(length = 10)
			private String name;

			public User(String name) {
				this.name = name;
			}
		}

		class User2 {
			@Order(1)
			@Message(length = 10, justify = Justify.RIGHT)
			private String name;

			public User2(String name) {
				this.name = name;
			}
		}
	}

}
