package com.github.ghkvud2.ft4j.unmarshall;

import java.lang.reflect.*;
import java.util.List;

import com.github.ghkvud2.ft4j.exception.MissingDefaultConstructorException;
import com.github.ghkvud2.ft4j.property.AnnotationFieldProperty;
import com.github.ghkvud2.ft4j.property.factory.PropertyFactory;
import com.github.ghkvud2.ft4j.util.AnnotationUtils;

public class DefaultUnMarshallManager implements UnMarshallManager {

	private final PropertyFactory propertyFactory;
	private final UnMarshaller unMarshaller;

	public DefaultUnMarshallManager(PropertyFactory propertyFactory, UnMarshaller unMarshaller) {
		this.propertyFactory = propertyFactory;
		this.unMarshaller = unMarshaller;
	}

	@Override
	public <T> T unmarshall(byte[] bytes, Class<T> clazz) {

		try {
			Constructor<T> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			T obj = constructor.newInstance();
			process(bytes, 0, obj);
			return obj;
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException | InstantiationException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new MissingDefaultConstructorException("Missing Default Constructor.", e);
		}
	}

	private int process(byte[] bytes, int offset, Object obj) throws IllegalArgumentException, IllegalAccessException,
			NoSuchMethodException, SecurityException, InstantiationException, InvocationTargetException {

        List<Field> fields = AnnotationUtils.getDeclaredFieldsWithOrderProperty(obj);
        
		for (Field field : fields) {

			if (field.isSynthetic()) {
				continue;
			}

			field.setAccessible(true);
			Class<?> fieldType = field.getType();

			if (fieldType.isPrimitive()) {
				AnnotationFieldProperty property = propertyFactory.createProperty(obj, field);
				offset = unMarshaller.unmarshall(property, offset, bytes);
				field.set(obj, property.getFieldValue());
			} else if (fieldType.equals(String.class)) {
				AnnotationFieldProperty property = propertyFactory.createProperty(obj, field);
				offset = unMarshaller.unmarshall(property, offset, bytes);
				field.set(obj, property.getFieldValue());
			} else {
				try {
					Constructor<?> constructor = field.getType().getDeclaredConstructor();
					Object fieldObj = constructor.newInstance();
					offset = process(bytes, offset, fieldObj);
					field.set(obj, fieldObj);
				} catch (NoSuchMethodException e) {
					throw new MissingDefaultConstructorException("Missing Default Constructor.", e);
				}
			}
		}
		return offset;
	}

}
