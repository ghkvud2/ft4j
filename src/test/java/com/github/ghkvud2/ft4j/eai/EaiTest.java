package com.github.ghkvud2.ft4j.eai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.marshall.MarshallFactory;
import com.github.ghkvud2.ft4j.marshall.MarshallManager;
import com.github.ghkvud2.ft4j.util.StringUtils;

public class EaiTest {

	private MarshallManager marshaller;
	private ConverterType type;

	@BeforeEach
	void setUp() {
		type = ConverterType.EUC_KR;
		marshaller = MarshallFactory.builder().converter(type).build();

	}

	@Test
	void test() {
		EaiCommon<PEBER0000040T> data;
		data = new EaiCommon<>();
		PEBER0000040T body = new PEBER0000040T();
		body.setOPR_CD("20201372");
		body.setSBR_CD("020820");
		body.setCNT_SUM(1L);
		data.setBody(body);
		byte[] result = marshaller.marshall(data);
		System.out.println("[" + StringUtils.convert(result, type) + "]");
	}

	@Test
	void test2() {
		EaiCommon<PEBER0000040T> data;
		data = new EaiCommon<>();
//		PEBER0000040T body = new PEBER0000040T();
//		body.setOPR_CD("20201372");
//		body.setSBR_CD("020820");
//		body.setCNT_SUM(1L);
//		data.setBody(body);
		byte[] result = marshaller.marshall(data);
		System.out.println("[" + StringUtils.convert(result, type) + "]");
	}

}
