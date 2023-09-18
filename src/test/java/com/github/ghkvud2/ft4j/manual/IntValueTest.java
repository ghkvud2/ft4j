package com.github.ghkvud2.ft4j.manual;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.ghkvud2.ft4j.annotation.IntValue;
import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.marshall.MarshallFactory;
import com.github.ghkvud2.ft4j.marshall.MarshallManager;

public class IntValueTest {

	private MarshallManager marshallManager;
	private Product product;
	private Charset charset;

	@BeforeEach
	void setUp() {
		marshallManager = MarshallFactory.builder().converter(ConverterType.UTF_8).build();
		product = new Product(100);
		charset = Charset.forName("utf-8");
	}

	@Test
	void length_test() {
		byte[] expected = "100       ".getBytes(charset);
		byte[] result = marshallManager.marshall(product);
		assertEquals(10, result.length);
		assertArrayEquals(expected, result);
	}

	static class Product {

		@IntValue(order = 1, length = 10, justify = Justify.LEFT, paddingByte = PaddingByte.SPACE)
		private int price;

		public Product(int price) {
			this.price = price;
		}

		public int getPrice() {
			return price;
		}

	}

}
