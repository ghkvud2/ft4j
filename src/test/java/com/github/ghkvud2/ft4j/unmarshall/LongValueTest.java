package com.github.ghkvud2.ft4j.unmarshall;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import com.github.ghkvud2.ft4j.annotation.LongValue;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.marshall.MarshallFactory;
import com.github.ghkvud2.ft4j.marshall.MarshallManager;

@DisplayName("Unmarshall - @LongValue 테스트")
public class LongValueTest {

	private MarshallManager marshaller;
	private UnMarshallManager unMarshaller;
	private ConverterType type;

	@BeforeEach
	void setUp() {
		type = ConverterType.UTF_8;
		marshaller = MarshallFactory.builder().converter(type).build();
		unMarshaller = UnMarshallFactory.builder().converter(type).build();
	}

	@DisplayName("@LongValue 기본 테스트")
	@Test
	void basic() {
		LongTestClass input = new LongTestClass(1L, 2L, 3L);
		byte[] result = marshaller.marshall(input);
		LongTestClass expected = unMarshaller.unmarshall(result, LongTestClass.class);
		equalsObject(expected, input);
		print(input, expected);
	}

	@DisplayName("@LongValue 여러 속성 설정 후, 테스트")
	@Test
	void test() {
		LongTestClass2 input = new LongTestClass2(1L, 2L, 3L);
		byte[] result = marshaller.marshall(input);
		LongTestClass2 expected = unMarshaller.unmarshall(result, LongTestClass2.class);
		equalsObject(expected, input);
		print(input, expected);
	}

	static class LongTestClass {

		@LongValue(order = 1, length = 1)
		private long num1;

		@LongValue(order = 2, length = 5)
		private long num2;

		@LongValue(order = 3, length = 10)
		private long num3;

		public LongTestClass(long num1, long num2, long num3) {
			this.num1 = num1;
			this.num2 = num2;
			this.num3 = num3;
		}

		public LongTestClass() {
		}

		@Override
		public String toString() {
			return "LongTestClass [num1=" + num1 + ", num2=" + num2 + ", num3=" + num3 + "]";
		}
	}

	static class LongTestClass2 {

		@LongValue(order = 1, length = 1, defaultValue = "9")
		private long num1;

		@LongValue(order = 2, length = 5, paddingByte = PaddingByte.SPACE)
		private long num2;

		@LongValue(order = 3, length = 10, paddingByte = PaddingByte.SPACE, justify = Justify.LEFT)
		private long num3;

		public LongTestClass2(long num1, long num2, long num3) {
			this.num1 = num1;
			this.num2 = num2;
			this.num3 = num3;
		}

		public LongTestClass2() {
		}

		@Override
		public String toString() {
			return "LongTestClass2 [num1=" + num1 + ", num2=" + num2 + ", num3=" + num3 + "]";
		}

	}

	private void equalsObject(LongTestClass expected, LongTestClass inputClass) {
		assertEquals(expected.num1, inputClass.num1);
		assertEquals(expected.num2, inputClass.num2);
		assertEquals(expected.num3, inputClass.num3);
	}

	private void equalsObject(LongTestClass2 expected, LongTestClass2 inputClass) {
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
