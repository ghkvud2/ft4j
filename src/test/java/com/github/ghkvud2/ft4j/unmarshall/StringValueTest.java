package com.github.ghkvud2.ft4j.unmarshall;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import com.github.ghkvud2.ft4j.annotation.StringValue;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.marshall.MarshallFactory;
import com.github.ghkvud2.ft4j.marshall.MarshallManager;

@DisplayName("Unmarshall - @StringValue 테스트")
public class StringValueTest {

	private MarshallManager marshaller;
	private UnMarshallManager unMarshaller;
	private ConverterType type;

	@BeforeEach
	void setUp() {
		type = ConverterType.EUC_KR;
		marshaller = MarshallFactory.builder().converter(type).build();
		unMarshaller = UnMarshallFactory.builder().converter(type).build();
	}

	@DisplayName("@StringValue 기본 테스트")
	@Test
	void basic_test() {
		StringValueTestClass input = new StringValueTestClass("가", "나다", "라마바");
		byte[] result = marshaller.marshall(input);
		StringValueTestClass expected = unMarshaller.unmarshall(result, StringValueTestClass.class);
		equalsObject(expected, input);
		print(input, expected);
	}

	@DisplayName("@StringValue 여러 속성 설정 후, 테스트")
	@Test
	void complex_test() {
		StringValueTestClass2 input = new StringValueTestClass2("가", "나다", "라마바");
		byte[] result = marshaller.marshall(input);
		StringValueTestClass2 expected = unMarshaller.unmarshall(result, StringValueTestClass2.class);
		equalsObject(expected, input);
		print(input, expected);
	}

	static class StringValueTestClass {

		@StringValue(order = 1, length = 2)
		private String num1;

		@StringValue(order = 2, length = 6)
		private String num2;

		@StringValue(order = 3, length = 10)
		private String num3;

		public StringValueTestClass(String num1, String num2, String num3) {
			this.num1 = num1;
			this.num2 = num2;
			this.num3 = num3;
		}

		public StringValueTestClass() {
		}

		@Override
		public String toString() {
			return "MessageTestClass [num1=" + num1 + ", num2=" + num2 + ", num3=" + num3 + "]";
		}
	}

	static class StringValueTestClass2 {

		@StringValue(order = 1, length = 10, defaultValue = "디폴트값")
		private String num1;

		@StringValue(order = 2, length = 15, paddingByte = PaddingByte.ZERO)
		private String num2;

		@StringValue(order = 3, length = 20, paddingByte = PaddingByte.ZERO, justify = Justify.RIGHT)
		private String num3;

		public StringValueTestClass2(String num1, String num2, String num3) {
			this.num1 = num1;
			this.num2 = num2;
			this.num3 = num3;
		}

		public StringValueTestClass2() {
		}

		@Override
		public String toString() {
			return "MessageTestClass2 [num1=" + num1 + ", num2=" + num2 + ", num3=" + num3 + "]";
		}
	}

	private void equalsObject(StringValueTestClass expected, StringValueTestClass inputClass) {
		assertEquals(expected.num1, inputClass.num1);
		assertEquals(expected.num2, inputClass.num2);
		assertEquals(expected.num3, inputClass.num3);
	}

	private void equalsObject(StringValueTestClass2 expected, StringValueTestClass2 inputClass) {
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
