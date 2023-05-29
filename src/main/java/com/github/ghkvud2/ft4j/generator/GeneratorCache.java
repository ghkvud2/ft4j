package com.github.ghkvud2.ft4j.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ghkvud2.ft4j.annotation.GeneratedValue;
import com.github.ghkvud2.ft4j.exception.*;

public class GeneratorCache {

	private static final Logger log = LoggerFactory.getLogger(GeneratorCache.class);
	private static final Map<Class<? extends Generator>, Generator> GENERATOR_CACHE = new HashMap<>();
	private static final ThreadLocal<Map<String, String>> THREAD_LOCAL_CACHE = new ThreadLocal<>();

	public static String getCachedValue(GeneratedValue generatedValue) {

		String key = generatedValue.key();
		boolean cacheable = generatedValue.cacheable();
		Generator generator = getCachedInstance(generatedValue.generator());

		if (cacheable) {

			if (key == null || key.isEmpty()) {
				throw new MissingCacheKeyException(
						"Missing cache key: When cacheable is set to true, a non-empty key must be provided for caching. Please specify a valid key.");
			}

			Map<String, String> cache = THREAD_LOCAL_CACHE.get();
			if (cache == null) {
				cache = new ConcurrentHashMap<>();
				THREAD_LOCAL_CACHE.set(cache);
			}

			if (!cache.containsKey(key) && generatedValue.generator().equals(NoopGenerator.class)) {
				throw new CacheKeyNotFoundException(String.format(
						"%s not found. Generator is required if you create it for the first time with that key value.",
						key));
			}

			if (cache.containsKey(key)) {
				String result = cache.get(key);
				log.debug("Return Cached value '{}'.", result);
				return result;
			}

			String result = generator.generate();
			log.debug("Create Cache value '{}' by {}", result, generatedValue.generator().getSimpleName());
			cache.put(key, result);
			return result;

		} else {

			if (generator instanceof NoopGenerator) {
				throw new MissingGeneratorException(
						"Missing generator configuration: when cacheable is set to false. Please provide a generator");
			}
			String result = generator.generate();
			log.debug("Not Cacheable, Create value '{}' by {}", result, generatedValue.generator().getSimpleName());
			return result;
		}
	}

	public static void clearCache() {
		THREAD_LOCAL_CACHE.remove();
	}

	private static Generator getCachedInstance(Class<? extends Generator> clazz) {

		try {
			if (GENERATOR_CACHE.containsKey(clazz)) {
				return GENERATOR_CACHE.get(clazz);
			}
			Constructor<? extends Generator> constructor = clazz.getDeclaredConstructor();
			Generator generatorInstance = constructor.newInstance();
			GENERATOR_CACHE.put(clazz, generatorInstance);
			return generatorInstance;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException e) {
			throw new RuntimeException("Error: Unable to execute the generator.", e);
		} catch (NoSuchMethodException e) {
			throw new MissingDefaultConstructorException("Missing Default Constructor.", e);
		}
	}

}