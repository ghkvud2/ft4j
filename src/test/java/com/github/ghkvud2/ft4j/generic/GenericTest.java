package com.github.ghkvud2.ft4j.generic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.ghkvud2.ft4j.annotation.IntValue;
import com.github.ghkvud2.ft4j.annotation.StringValue;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.marshall.MarshallFactory;
import com.github.ghkvud2.ft4j.marshall.MarshallManager;
import com.github.ghkvud2.ft4j.util.StringUtils;

public class GenericTest {

	private MarshallManager marshaller;
	private ConverterType type;

	@BeforeEach
	void setUp() {
		type = ConverterType.EUC_KR;
		marshaller = MarshallFactory.builder().converter(type).build();
	}

	@Test
	void test() {
		Common<String> comm = new Common<>();
		assertNull(comm.data);
		byte[] bytes = marshaller.marshall(comm);
		assertEquals("000", StringUtils.convert(bytes, type));
		assertNotNull(comm.data);
	}

	public class Common<T> {
		@IntValue(order = 1, length = 3)
		private int num;

		@StringValue(order = 2, length = 7)
		private T data;

	}

}
