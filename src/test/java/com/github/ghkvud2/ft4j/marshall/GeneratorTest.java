package com.github.ghkvud2.ft4j.marshall;

import static com.github.ghkvud2.ft4j.util.StringUtils.convert;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.*;

import com.github.ghkvud2.ft4j.annotation.GeneratedValue;
import com.github.ghkvud2.ft4j.annotation.StringValue;
import com.github.ghkvud2.ft4j.constant.ConverterType;
import com.github.ghkvud2.ft4j.exception.*;
import com.github.ghkvud2.ft4j.generator.Generator;

@DisplayName("@GeneratedValue 어노테이션")
public class GeneratorTest {

	private MarshallManager marshaller;
	protected static String uuid;
	private ConverterType type;
	boolean first;
	byte[] memory;

	@BeforeEach
	void setUp() {
		type = ConverterType.EUC_KR;
		first = true;
		memory = null;
		marshaller = MarshallFactory.builder().converter(type).build();
	}

	@DisplayName("Generator만 설정했을 때")
	@Test
	void generator_test() {
		TestClass testClass = new TestClass();
		byte[] result = marshaller.marshall(testClass);
		assertEquals(convert(result, type), testClass.uuid);
	}

	@DisplayName("generated value length > length property")
	@Test
	void generated_value_exceeds_limit_test() {
		TestClass2 testClass = new TestClass2();
		assertThrows(GeneratedValueExceedsLimitException.class, () -> marshaller.marshall(testClass));
	}

	@DisplayName("두 필드가 같은 Generator를 사용하고 cacheable = true로 설정했을 때, 각각의 필드가 같은 값을 갖는지 확인")
	@Test
	void cacheable_test() {
		TestClass3 testClass = new TestClass3();
		byte[] result = marshaller.marshall(testClass);
		assertEquals(convert(result, type), testClass.uuid + testClass.uuid2);
		assertEquals(testClass.uuid, testClass.uuid2);
	}

	@DisplayName("두 필드가 같은 Generator를 사용하고 cacheable = false로 설정했을 때, 각각의 필드가 다른 값을 갖는지 확인")
	@Test
	void not_cacheable_test() {
		TestClass4 testClass = new TestClass4();
		byte[] result = marshaller.marshall(testClass);
		assertEquals(convert(result, type), testClass.uuid + testClass.uuid2);
		assertNotEquals(testClass.uuid, testClass.uuid2);
	}

	@DisplayName("cacheable = true로 설정했는데, generator를 지정하지 않아 해당 key 값으로 저장된 value가 없을 때 예외 발생")
	@Test
	void key_not_found_test() {
		TestClass5 testClass = new TestClass5();
		assertThrows(CacheKeyNotFoundException.class, () -> marshaller.marshall(testClass));
	}

	@DisplayName("cacheable = true로 설정하고 key값이 설정되지 않았을 때, 예외 발생")
	@Test
	void missing_key_test() {
		TestClass6 testClass = new TestClass6();
		assertThrows(MissingCacheKeyException.class, () -> marshaller.marshall(testClass));
	}

	@DisplayName("ThreadSafe 테스트, 각 실행 결과는 독립적인 값을 가져야함")
	@Test
	void multithread_test() {

		TestClass3 testClass = new TestClass3();

		for (int i = 0; i < 20; i++) {

			Runnable thread = new Runnable() {

				@Override
				public void run() {
					byte[] result = marshaller.marshall(testClass);
					assertNotEquals(convert(memory, type), convert(result, type));

					if (first) {
						first = false;
						memory = result;
					}
					assertEquals(convert(result, type), testClass.uuid + testClass.uuid2);
					assertEquals(testClass.uuid, testClass.uuid2);
				}
			};

			thread.run();
		}
	}

	static class TestClass {

		@GeneratedValue(generator = UUIDGenerator.class)
		@StringValue(order = 1, length = 36)
		private String uuid;
	}

	static class TestClass2 {

		@GeneratedValue(generator = UUIDGenerator.class)
		@StringValue(order = 1, length = 32)
		private String uuid;
	}

	static class TestClass3 {

		@GeneratedValue(key = "uuid", cacheable = true, generator = UUIDGenerator.class)
		@StringValue(order = 1, length = 36)
		private String uuid;

		@GeneratedValue(key = "uuid", cacheable = true)
		@StringValue(order = 2, length = 36)
		private String uuid2;
	}

	static class TestClass4 {

		@GeneratedValue(key = "uuid", cacheable = true, generator = UUIDGenerator.class)
		@StringValue(order = 1, length = 36)
		private String uuid;

		@GeneratedValue(key = "uuid", generator = UUIDGenerator.class)
		@StringValue(order = 2, length = 36)
		private String uuid2;
	}

	static class TestClass5 {

		@GeneratedValue(key = "uuid", cacheable = true)
		@StringValue(order = 1, length = 36)
		private String uuid;
	}

	static class TestClass6 {

		@GeneratedValue(cacheable = true)
		@StringValue(order = 1, length = 36)
		private String uuid;
	}

	public static class UUIDGenerator implements Generator {

		@Override
		public String generate() {
			return UUID.randomUUID().toString();
		}
	}
}
