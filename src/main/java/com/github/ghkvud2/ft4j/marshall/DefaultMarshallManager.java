package com.github.ghkvud2.ft4j.marshall;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.List;

import com.github.ghkvud2.ft4j.exception.MissingDefaultConstructorException;
import com.github.ghkvud2.ft4j.generator.GeneratorCache;
import com.github.ghkvud2.ft4j.property.AnnotationFieldProperty;
import com.github.ghkvud2.ft4j.property.factory.PropertyFactory;
import com.github.ghkvud2.ft4j.util.AnnotationUtils;
import com.github.ghkvud2.ft4j.validator.ValidatorManager;

public class DefaultMarshallManager implements MarshallManager {

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
	public byte[] marshall(Object obj) {

		try {
			return process(obj);
		} catch (NumberFormatException e) {
			throw e;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} finally {
			GeneratorCache.clearCache();
		}
	}

	private byte[] process(Object obj) throws IllegalArgumentException, IllegalAccessException, IOException,
			NoSuchMethodException, SecurityException, InvocationTargetException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		List<Field> fields = AnnotationUtils.getDeclaredFieldsWithOrderProperty(obj);

		for (Field field : fields) {

			if (field.isSynthetic()) {
				continue;
			}

			field.setAccessible(true);

			Class<?> fieldType = field.getType();

			if (fieldType.isPrimitive() || fieldType.equals(String.class)) {
				AnnotationFieldProperty property = propertyFactory.createProperty(obj, field);
				validatorManager.preValidate(property);
				byte[] bytes = marshaller.marshall(property);
				field.set(obj, property.getFieldValue());
				validatorManager.postValidate(property, bytes);
				outputStream.write(bytes);
			} else {
				Object newObject = field.get(obj);

				if (newObject == null) {
					newObject = createObject(fieldType);
				}

				field.set(obj, newObject);
				outputStream.write(process(newObject));
			}
		}
		return outputStream.toByteArray();
	}

	private Object createObject(Class<?> fieldType) {
		try {
			Constructor<?> constructor = fieldType.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to create a new instance of the class: " + fieldType.getName(), e);
		} catch (NoSuchMethodException e) {
			throw new MissingDefaultConstructorException("Missing Default Constructor.", e);
		}
	}
}
