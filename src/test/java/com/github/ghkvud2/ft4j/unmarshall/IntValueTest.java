package com.github.ghkvud2.ft4j.unmarshall;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import com.github.ghkvud2.ft4j.annotation.IntValue;
import com.github.ghkvud2.ft4j.annotation.Order;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.marshall.MarshallFactory;
import com.github.ghkvud2.ft4j.marshall.MarshallManager;

@DisplayName("Unmarshall - @IntValue 테스트")
public class IntValueTest {

	private MarshallManager marshaller;
	private UnMarshallManager unMarshaller;
	private ConverterType type;

	@BeforeEach
	void setUp() {
		type = ConverterType.UTF_8;
		marshaller = MarshallFactory.builder().converter(type).build();
		unMarshaller = UnMarshallFactory.builder().converter(type).build();
	}

	@DisplayName("@IntValue 테스트")
	@Test
	void basic() {
		IntTestClass input = new IntTestClass(1, 2, 3);
		byte[] result = marshaller.marshall(input);
		IntTestClass expected = unMarshaller.unmarshall(result, IntTestClass.class);
		equalsObject(expected, input);
		print(input, expected);
	}

	@DisplayName("@IntValue 여러 속성 설정 후, 테스트")
	@Test
	void complex() {
		IntTestClass2 input = new IntTestClass2(1, 2, 3);
		byte[] result = marshaller.marshall(input);
		IntTestClass2 expected = unMarshaller.unmarshall(result, IntTestClass2.class);
		equalsObject(expected, input);
		print(input, expected);
	}

	static class IntTestClass {

		@Order(1)
		@IntValue(length = 1)
		private int num1;

		@Order(2)
		@IntValue(length = 5)
		private int num2;

		@Order(3)
		@IntValue(length = 10)
		private int num3;

		public IntTestClass(int num1, int num2, int num3) {
			this.num1 = num1;
			this.num2 = num2;
			this.num3 = num3;
		}

		public IntTestClass() {
		}

		@Override
		public String toString() {
			return "IntTestClass [num1=" + num1 + ", num2=" + num2 + ", num3=" + num3 + "]";
		}
	}

	static class IntTestClass2 {

		@Order(1)
		@IntValue(length = 1, defaultValue = "9")
		private int num1;

		@Order(2)
		@IntValue(length = 5, paddingByte = PaddingByte.SPACE)
		private int num2;

		@Order(3)
		@IntValue(length = 10, paddingByte = PaddingByte.SPACE, justify = Justify.LEFT)
		private int num3;

		public IntTestClass2(int num1, int num2, int num3) {
			this.num1 = num1;
			this.num2 = num2;
			this.num3 = num3;
		}

		public IntTestClass2() {
		}

		@Override
		public String toString() {
			return "IntTestClass2 [num1=" + num1 + ", num2=" + num2 + ", num3=" + num3 + "]";
		}
	}

	private void equalsObject(IntTestClass expected, IntTestClass inputClass) {
		assertEquals(expected.num1, inputClass.num1);
		assertEquals(expected.num2, inputClass.num2);
		assertEquals(expected.num3, inputClass.num3);
	}

	private void equalsObject(IntTestClass2 expected, IntTestClass2 inputClass) {
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
