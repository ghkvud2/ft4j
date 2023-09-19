package com.github.ghkvud2.ft4j.manual;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.Charset;

import org.junit.jupiter.api.*;

import com.github.ghkvud2.ft4j.annotation.StringValue;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.marshall.MarshallFactory;
import com.github.ghkvud2.ft4j.marshall.MarshallManager;
import com.github.ghkvud2.ft4j.unmarshall.UnMarshallFactory;
import com.github.ghkvud2.ft4j.unmarshall.UnMarshallManager;

public class EncodingTest {

	private MarshallManager marshallManager;
	private UnMarshallManager unMarshallManager;

	@BeforeEach
	void setUp() {
		marshallManager = MarshallFactory.builder().converter(ConverterType.EUC_KR).build();
		unMarshallManager = UnMarshallFactory.builder().converter(ConverterType.EUC_KR).build();
	}

	@Disabled
	@Test
	void euckr_kor_test() {
		byte[] input = "스미스".getBytes(Charset.forName("euc-kr"));
		User user = unMarshallManager.unmarshall(input, User.class);
		assertEquals("스미", user.getName());
	}

	static class User {

		@StringValue(order = 1, length = 5)
		private String name;

		public User(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public User() {
			super();
		}

	}

}
