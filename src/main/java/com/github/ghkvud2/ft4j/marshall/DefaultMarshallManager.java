package com.github.ghkvud2.ft4j.marshall;

import java.lang.reflect.*;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ghkvud2.ft4j.exception.MissingDefaultConstructorException;
import com.github.ghkvud2.ft4j.generator.GeneratorCache;
import com.github.ghkvud2.ft4j.property.AnnotationFieldProperty;
import com.github.ghkvud2.ft4j.property.factory.PropertyFactory;
import com.github.ghkvud2.ft4j.util.AnnotationUtils;
import com.github.ghkvud2.ft4j.validator.ValidatorManager;

public class DefaultMarshallManager implements MarshallManager {

	private static final Logger log = LoggerFactory.getLogger(DefaultMarshallManager.class);
	private final PropertyFactory propertyFactory;
	private final Marshaller marshaller;
	private final ValidatorManager validatorManager;

	public DefaultMarshallManager(PropertyFactory propertyFactory, Marshaller marshaller,
			ValidatorManager validatorManager) {
		this.propertyFactory = propertyFactory;
		this.marshaller = marshaller;
		this.validatorManager = validatorManager;
	}

	@Override
	public String marshall(Object obj) {

		try {
			return process(obj);
		} catch (NumberFormatException e) {
			throw e;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		} finally {
			GeneratorCache.clearCache();
		}
	}

	private String process(Object obj) throws IllegalArgumentException, IllegalAccessException {
		StringBuilder sb = new StringBuilder();

		List<Field> fields = AnnotationUtils.getDeclaredFieldsOrdering(obj);

		for (Field field : fields) {

			if (field.isSynthetic()) {
				continue;
			}

			field.setAccessible(true);

			Class<?> fieldType = field.getType();

			if (fieldType.isPrimitive() || fieldType.equals(String.class)) {
				AnnotationFieldProperty property = propertyFactory.createProperty(obj, field);
				validatorManager.preValidate(property);
				String result = marshaller.marshall(property);
				field.set(obj, property.getFieldValue());
				validatorManager.postValidate(property, result);
				sb.append(result);
			} else {
				Object newObject = field.get(obj);
				if (newObject == null) {
					newObject = createObject(fieldType);
				}
				sb.append(process(newObject));
			}
		}
		log.info("result={}", marshaller.convertWithCharset(sb.toString()));
		return marshaller.convertWithCharset(sb.toString());
	}

	private Object createObject(Class<?> fieldType) {
		try {
			Constructor<?> constructor = fieldType.getDeclaredConstructor();
			return constructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to create a new instance of the class: " + fieldType.getName(), e);
		} catch (NoSuchMethodException e) {
			throw new MissingDefaultConstructorException("Missing Default Constructor.", e);
		}
	}
}
