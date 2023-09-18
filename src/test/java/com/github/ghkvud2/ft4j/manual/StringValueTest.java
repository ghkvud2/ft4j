package com.github.ghkvud2.ft4j.manual;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.ghkvud2.ft4j.annotation.StringValue;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.marshall.MarshallFactory;
import com.github.ghkvud2.ft4j.marshall.MarshallManager;

public class StringValueTest {

	private MarshallManager marshallManager;
	private User user;
	private Charset charset;

	@BeforeEach
	void setUp() {
		marshallManager = MarshallFactory.builder().converter(ConverterType.UTF_8).build();
		user = new User("John");
		charset = Charset.forName("utf-8");
	}

	@Test
	void length_test() {

		byte[] expected = "my name        ".getBytes(charset);
		byte[] result = marshallManager.marshall(user);

		assertEquals(15, result.length);
		assertArrayEquals(expected, result);

//		byte[] expected = "Joh".getBytes(charset);
//		byte[] result = marshallManager.marshall(user);
//
//		assertEquals(3, result.length);
//		assertArrayEquals(expected, result);

	}

	@Test
	void default_padding_test() {
		byte[] expected = "           John".getBytes(charset);
		byte[] result = marshallManager.marshall(user);

		assertEquals(15, result.length);
		assertArrayEquals(expected, result);
	}

	static class User {

		@StringValue(order = 1, length = 15, justify = Justify.RIGHT)
		private String name;

		public User(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}
}
