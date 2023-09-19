package com.github.ghkvud2.ft4j.manual;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.Charset;

import org.junit.jupiter.api.*;

import com.github.ghkvud2.ft4j.annotation.GeneratedValue;
import com.github.ghkvud2.ft4j.annotation.StringValue;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.exception.GeneratedValueExceedsLimitException;
import com.github.ghkvud2.ft4j.marshall.MarshallFactory;
import com.github.ghkvud2.ft4j.marshall.MarshallManager;

public class GeneratorTest {

	private MarshallManager marshallManager;
	private Payment payment;
	private Charset charset;

	@BeforeEach
	void setUp() {
		marshallManager = MarshallFactory.builder().converter(ConverterType.UTF_8).build();
		payment = new Payment();
		charset = Charset.forName("utf-8");
	}

	@Disabled
	@Test
	void generator_test() {
	    byte[] result = marshallManager.marshall(payment);
	    System.out.println("실행 결과 : " + new String(result, charset));
	}
	
	@Disabled
	@Test
	void generated_value_exceeds_limit_test() {
	    assertThrows(GeneratedValueExceedsLimitException.class, () -> marshallManager.marshall(payment));
	}
	
	static class Payment {

		@GeneratedValue(generator = FullDateTimeGenerator.class)
		@StringValue(order = 1, length = 16)
		private String paymentDate;

		public String getPaymentDate() {
			return paymentDate;
		}

	}

}
