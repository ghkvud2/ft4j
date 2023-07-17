package com.github.ghkvud2.ft4j.unmarshall;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import com.github.ghkvud2.ft4j.annotation.ShortValue;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.marshall.MarshallFactory;
import com.github.ghkvud2.ft4j.marshall.MarshallManager;

@DisplayName("Unmarshall - @ShortValue 테스트")
public class ShortValueTest {

	private MarshallManager marshaller;
	private UnMarshallManager unMarshaller;
	private ConverterType type;

	@BeforeEach
	void setUp() {
		type = ConverterType.UTF_8;
		marshaller = MarshallFactory.builder().converter(type).build();
		unMarshaller = UnMarshallFactory.builder().converter(type).build();
	}

	@DisplayName("@ShortValue 기본 테스트")
	@Test
	void basic() {
		ShortTestClass input = new ShortTestClass((short) 1, (short) 2, (short) 3);
		byte[] result = marshaller.marshall(input);
		ShortTestClass expected = unMarshaller.unmarshall(result, ShortTestClass.class);
		equalsObject(expected, input);
		print(input, expected);
	}

	@DisplayName("@ShortValue 여러 속성 설정 후, 테스트")
	@Test
	void complex() {
		ShortTestClass2 input = new ShortTestClass2((short) 1, (short) 2, (short) 3);
		byte[] result = marshaller.marshall(input);
		ShortTestClass2 expected = unMarshaller.unmarshall(result, ShortTestClass2.class);
		equalsObject(expected, input);
		print(input, expected);

	}

	static class ShortTestClass {

		@ShortValue(order = 1, length = 1)
		private short num1;

		@ShortValue(order = 2, length = 5)
		private short num2;

		@ShortValue(order = 3, length = 10)
		private short num3;

		public ShortTestClass(short num1, short num2, short num3) {
			this.num1 = num1;
			this.num2 = num2;
			this.num3 = num3;
		}

		public ShortTestClass() {
		}

		@Override
		public String toString() {
			return "ShortTestClass [num1=" + num1 + ", num2=" + num2 + ", num3=" + num3 + "]";
		}

	}

	static class ShortTestClass2 {

		@ShortValue(order = 1, length = 1, defaultValue = "9")
		private short num1;

		@ShortValue(order = 2, length = 5, paddingByte = PaddingByte.SPACE)
		private short num2;

		@ShortValue(order = 3, length = 10, paddingByte = PaddingByte.SPACE, justify = Justify.LEFT)
		private short num3;

		public ShortTestClass2(short num1, short num2, short num3) {
			this.num1 = num1;
			this.num2 = num2;
			this.num3 = num3;
		}

		public ShortTestClass2() {
		}

		@Override
		public String toString() {
			return "ShortTestClass2 [num1=" + num1 + ", num2=" + num2 + ", num3=" + num3 + "]";
		}

	}

	private void equalsObject(ShortTestClass expected, ShortTestClass inputClass) {
		assertEquals(expected.num1, inputClass.num1);
		assertEquals(expected.num2, inputClass.num2);
		assertEquals(expected.num3, inputClass.num3);
	}

	private void equalsObject(ShortTestClass2 expected, ShortTestClass2 inputClass) {
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
