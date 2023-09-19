package com.github.ghkvud2.ft4j.manual;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.Charset;

import org.junit.jupiter.api.*;

import com.github.ghkvud2.ft4j.annotation.DoubleValue;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.marshall.MarshallFactory;
import com.github.ghkvud2.ft4j.marshall.MarshallManager;

public class DoubleTest {

	private MarshallManager marshallManager;
	private Bank bank;
	private Charset charset;

	@BeforeEach
	void setUp() {
		marshallManager = MarshallFactory.builder().converter(ConverterType.UTF_8).build();
		bank = new Bank(135.8345);
		charset = Charset.forName("utf-8");
	}

	@Disabled
	@Test
	void length_test() {
		String expected = "135.8345";
		byte[] result = marshallManager.marshall(bank);
		assertEquals(8, result.length);
		assertEquals(expected, convert(result, charset));
	}

	static class Bank {

		@DoubleValue(order = 1, length = 8)
		private double rate;

		public Bank(double rate) {
			this.rate = rate;
		}

		public double getRate() {
			return rate;
		}
	}

	public static String convert(byte[] bytes, Charset charset) {
		return new String(bytes, charset);
	}
}
