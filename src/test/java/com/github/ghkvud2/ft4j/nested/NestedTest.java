package com.github.ghkvud2.ft4j.nested;

import static com.github.ghkvud2.ft4j.util.StringUtils.convert;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.marshall.MarshallFactory;
import com.github.ghkvud2.ft4j.marshall.MarshallManager;
import com.github.ghkvud2.ft4j.nested.domain.Address;
import com.github.ghkvud2.ft4j.nested.domain.User;
import com.github.ghkvud2.ft4j.unmarshall.UnMarshallFactory;
import com.github.ghkvud2.ft4j.unmarshall.UnMarshallManager;

public class NestedTest {

	private MarshallManager marshaller;
	private UnMarshallManager unMarshaller;
	private User input;
	private ConverterType type;

	@BeforeEach
	void setUp() {
		type = ConverterType.EUC_KR;
		marshaller = MarshallFactory.builder().converter(type).build();
		unMarshaller = UnMarshallFactory.builder().converter(type).build();
		Address address = new Address("서울특별시", "양천구", "신월동");
		input = new User("홍길동", 34, "honggil@gmail.com", address);
	}

	@Test
	void nested() {
		byte[] result = marshaller.marshall(input);
		User expected = unMarshaller.unmarshall(convert(result, type), User.class);
		assertObject(expected, input);
	}

	private void assertObject(User expected, User input) {
		assertEquals(expected.getName(), input.getName());
		assertEquals(expected.getAge(), input.getAge());
		assertEquals(expected.getEmail(), input.getEmail());
		assertEquals(expected.getAddress().getAddr1(), input.getAddress().getAddr1());
		assertEquals(expected.getAddress().getAddr2(), input.getAddress().getAddr2());
		assertEquals(expected.getAddress().getAddr3(), input.getAddress().getAddr3());
	}
}
