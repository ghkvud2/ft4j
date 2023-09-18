package com.github.ghkvud2.ft4j.manual;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

import com.github.ghkvud2.ft4j.annotation.StringValue;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.marshall.MarshallFactory;
import com.github.ghkvud2.ft4j.marshall.MarshallManager;
import com.github.ghkvud2.ft4j.unmarshall.UnMarshallFactory;
import com.github.ghkvud2.ft4j.unmarshall.UnMarshallManager;

public class PaddingByteTest {

	@Test
	void length_marshall_test() {

		MarshallManager marshaller = MarshallFactory.builder().converter(ConverterType.UTF_8).build();

		Person person = new Person("John");
		byte[] expected = "John000000".getBytes(Charset.forName("utf-8"));
		byte[] result = marshaller.marshall(person);

		assertArrayEquals(expected, result);
		assertEquals(10, result.length);
	}

	@Test
	void length_unmarshall_test() {

		UnMarshallManager unMarshaller = UnMarshallFactory.builder().converter(ConverterType.UTF_8).build();

		byte[] input = "John      ".getBytes(Charset.forName("utf-8"));

		assertEquals(10, input.length);
		Person person = unMarshaller.unmarshall(input, Person.class);
		assertEquals("John      ", person.getName());
		
		
		input = "John000000".getBytes(Charset.forName("utf-8"));
		person = unMarshaller.unmarshall(input, Person.class);
		assertEquals("John", person.getName());
	}

	static class Person {

		@StringValue(order = 1, length = 10, paddingByte = PaddingByte.ZERO)
		private String name;

		public Person(String name) {
			this.name = name;
		}

		public Person() {
			super();
		}

		public String getName() {
			return name;
		}

	}
}
