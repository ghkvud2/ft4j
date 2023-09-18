package com.github.ghkvud2.ft4j.manual;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.ghkvud2.ft4j.annotation.GeneratedValue;
import com.github.ghkvud2.ft4j.annotation.StringValue;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.exception.CacheKeyNotFoundException;
import com.github.ghkvud2.ft4j.marshall.GeneratorTest.UUIDGenerator;
import com.github.ghkvud2.ft4j.marshall.MarshallFactory;
import com.github.ghkvud2.ft4j.marshall.MarshallManager;

public class CacheTest {

	private MarshallManager marshallManager;
	private Payment payment;
	private Charset charset;

	@BeforeEach
	void setUp() {
		marshallManager = MarshallFactory.builder().converter(ConverterType.UTF_8).build();
		payment = new Payment();
		charset = Charset.forName("utf-8");
	}

	@Test
	void not_cacheable_test() {
		byte[] result = marshallManager.marshall(payment);
		System.out.println("실행 결과 : " + new String(result, charset));
	}

	@Test
	void key_not_found_test() {
		assertThrows(CacheKeyNotFoundException.class, () -> marshallManager.marshall(payment));
	}
	
	@Test
	void multithread_test() {

	    for(int i = 0 ; i < 5; i++) {
	        Runnable thread = () -> {
	        	byte[] result = marshallManager.marshall(payment);
	    		System.out.println("실행 결과 : " + new String(result, charset));
	        };
	        thread.run();
	    }
	}

	static class Payment {
		@GeneratedValue(key = "id", cacheable = true, generator = UUIDGenerator.class)
		@StringValue(order = 1, length = 36)
		private String uuid;

		@StringValue(order = 2, length = 5)
		private String space;

		@GeneratedValue(key = "id", cacheable = true)
		@StringValue(order = 3, length = 36)
		private String cachedId;
	}

}
