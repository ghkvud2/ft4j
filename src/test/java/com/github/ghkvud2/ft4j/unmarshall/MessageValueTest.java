package com.github.ghkvud2.ft4j.unmarshall;

import static com.github.ghkvud2.ft4j.util.StringUtils.convert;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import com.github.ghkvud2.ft4j.annotation.Message;
import com.github.ghkvud2.ft4j.annotation.Order;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.marshall.MarshallFactory;
import com.github.ghkvud2.ft4j.marshall.MarshallManager;
import com.github.ghkvud2.ft4j.unmarshall.UnMarshallFactory;
import com.github.ghkvud2.ft4j.unmarshall.UnMarshallManager;

@DisplayName("Unmarshall - @Message 테스트")
public class MessageValueTest {

	private MarshallManager marshaller;
	private UnMarshallManager unMarshaller;
	private ConverterType type;

	@BeforeEach
	void setUp() {
		type = ConverterType.EUC_KR;
		marshaller = MarshallFactory.builder().converter(type).build();
		unMarshaller = UnMarshallFactory.builder().converter(type).build();
	}

	@DisplayName("@Message 기본 테스트")
	@Test
	void basic_test() {
		MessageTestClass input = new MessageTestClass("가", "나다", "라마바");
		byte[] result = marshaller.marshall(input);
		MessageTestClass expected = unMarshaller.unmarshall(convert(result, type), MessageTestClass.class);
		equalsObject(expected, input);
		print(input, expected);
	}

	@DisplayName("@Message 여러 속성 설정 후, 테스트")
	@Test
	void complex_test() {
		MessageTestClass2 input = new MessageTestClass2("가", "나다", "라마바");
		byte[] result = marshaller.marshall(input);
		MessageTestClass2 expected = unMarshaller.unmarshall(convert(result, type), MessageTestClass2.class);
		equalsObject(expected, input);
		print(input, expected);
	}

	static class MessageTestClass {

		@Order(1)
		@Message(length = 2)
		private String num1;

		@Order(1)
		@Message(length = 6)
		private String num2;

		@Order(3)
		@Message(length = 10)
		private String num3;

		public MessageTestClass(String num1, String num2, String num3) {
			this.num1 = num1;
			this.num2 = num2;
			this.num3 = num3;
		}

		public MessageTestClass() {
		}

		@Override
		public String toString() {
			return "MessageTestClass [num1=" + num1 + ", num2=" + num2 + ", num3=" + num3 + "]";
		}
	}

	static class MessageTestClass2 {

		@Order(1)
		@Message(length = 10, defaultValue = "디폴트값")
		private String num1;

		@Order(2)
		@Message(length = 15, paddingByte = PaddingByte.ZERO)
		private String num2;

		@Order(3)
		@Message(length = 20, paddingByte = PaddingByte.ZERO, justify = Justify.RIGHT)
		private String num3;

		public MessageTestClass2(String num1, String num2, String num3) {
			this.num1 = num1;
			this.num2 = num2;
			this.num3 = num3;
		}

		public MessageTestClass2() {
		}

		@Override
		public String toString() {
			return "MessageTestClass2 [num1=" + num1 + ", num2=" + num2 + ", num3=" + num3 + "]";
		}
	}

	private void equalsObject(MessageTestClass expected, MessageTestClass inputClass) {
		assertEquals(expected.num1, inputClass.num1);
		assertEquals(expected.num2, inputClass.num2);
		assertEquals(expected.num3, inputClass.num3);
	}

	private void equalsObject(MessageTestClass2 expected, MessageTestClass2 inputClass) {
		assertEquals(expected.num1, inputClass.num1);
		assertEquals(expected.num2, inputClass.num2);
		assertEquals(expected.num3, inputClass.num3);
	}

	private void print(Object... objs) {
		for (Object obj : objs) {
			System.out.println(obj);
		}
		System.out.println();
	}

}
