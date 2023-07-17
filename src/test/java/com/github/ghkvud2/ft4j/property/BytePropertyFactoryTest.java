package com.github.ghkvud2.ft4j.property;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;

import org.junit.jupiter.api.*;

import com.github.ghkvud2.ft4j.annotation.*;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.property.factory.BytePropertyFactory;
import com.github.ghkvud2.ft4j.property.factory.PropertyFactory;

@DisplayName("BytePropertyFactory 테스트")
class BytePropertyFactoryTest {

	private PropertyFactory propertyFactory;
	private Field field;
	private AbstractProperty<?> property;
	private TestClass obj;

	@BeforeEach
	void setUp() {
		propertyFactory = new BytePropertyFactory();
		obj = new TestClass((short) 1, 2, 3L, 4.23, 5.5);
	}

	@SuppressWarnings("unchecked")
	@DisplayName("AbstractBytesProperty 생성 테스트")
	@Test
	void abstractBytesProperty_test() throws NoSuchFieldException, SecurityException {

		field = obj.getClass().getDeclaredField("num1");
		property = (AbstractProperty<Short>) propertyFactory.createProperty(obj, field);
		assertProperty(property, 3, "33", true, PaddingByte.ZERO, Justify.RIGHT, (short) 33);

		field = obj.getClass().getDeclaredField("num2");
		property = (AbstractProperty<Integer>) propertyFactory.createProperty(obj, field);
		assertProperty(property, 4, "", false, PaddingByte.SPACE, Justify.RIGHT, 2);

		field = obj.getClass().getDeclaredField("num3");
		property = (AbstractProperty<Long>) propertyFactory.createProperty(obj, field);
		assertProperty(property, 5, "", false, PaddingByte.ZERO, Justify.LEFT, 3L);

		field = obj.getClass().getDeclaredField("num4");
		property = (AbstractProperty<Double>) propertyFactory.createProperty(obj, field);
		assertProperty(property, 6, "", false, PaddingByte.ZERO, Justify.RIGHT, 4.23);

		field = obj.getClass().getDeclaredField("num5");
		property = (AbstractProperty<Double>) propertyFactory.createProperty(obj, field);
		assertProperty(property, 7, "3.33", false, PaddingByte.ZERO, Justify.RIGHT, 3.33);

	}

	private void assertProperty(AbstractProperty<?> property, int length, String defaultValue, boolean ignoreLimit,
			PaddingByte padding, Justify justify, Object fieldValue) {
		assertEquals(property.length(), length);
		assertEquals(property.defaultValue(), defaultValue);
		assertEquals(property.ignoreLimit(), ignoreLimit);
		assertEquals(property.padding(), padding);
		assertEquals(property.justify(), justify);
		assertEquals(property.getFieldValue(), fieldValue);
	}

	@SuppressWarnings("unused")
	private void print(AbstractProperty<?> property) {
		System.out.println("length : " + property.length());
		System.out.println("defaultValue : " + property.defaultValue());
		System.out.println("justify : " + property.justify());
		System.out.println("ignoreLimit : " + property.ignoreLimit());
		System.out.println("getFieldValue : " + property.getFieldValue());
		System.out.println();
	}

	static class TestClass {

		@ShortValue(order = 1, length = 3, defaultValue = "33", ignoreLimit = true)
		private short num1;

		@IntValue(order = 2, length = 4, paddingByte = PaddingByte.SPACE)
		private int num2;

		@LongValue(order = 3, length = 5, justify = Justify.LEFT)
		private long num3;

		@DoubleValue(order = 4, length = 6)
		private double num4;

		@DoubleValue(order = 5, length = 7, defaultValue = "3.33")
		private double num5;

		public TestClass(short num1, int num2, long num3, double num4, double num5) {
			super();
			this.num1 = num1;
			this.num2 = num2;
			this.num3 = num3;
			this.num4 = num4;
			this.num5 = num5;
		}

	}

}
