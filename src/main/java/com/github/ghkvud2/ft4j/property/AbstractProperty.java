package com.github.ghkvud2.ft4j.property;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.github.ghkvud2.ft4j.annotation.GeneratedValue;
import com.github.ghkvud2.ft4j.generator.GeneratorCache;

public abstract class AbstractProperty<T> implements AnnotationFieldProperty {

	protected final Field field;
	protected Object obj;

	protected T fieldValue;
	protected Annotation annotation;
	private GeneratedValue generatedValue;

	private boolean useDefaultValue;
	private boolean useGenerator;

	public AbstractProperty(Object obj, Field field) {
		this.obj = obj;
		this.field = field;
		init();
	}
	
	public AbstractProperty(Field field) {
		this.field = field;
		init();
	}

	private void init() {
		this.field.setAccessible(true);
		this.annotation = field.getDeclaredAnnotation(getAnnotationType());
		this.generatedValue = field.getDeclaredAnnotation(GeneratedValue.class);

		if (generatedValue != null) {
			String generatedCacheValue = GeneratorCache.getCachedValue(generatedValue);
			setFieldValueFromString(generatedCacheValue);
			useGenerator = true;
		} else if (defaultValue() != null && !defaultValue().isEmpty()) {
			setFieldValueFromString(defaultValue());
			useDefaultValue = true;
		} else {
			setFieldValueFromObject();
		}
	}

	public Field getField() {
		return this.field;
	}

	@Override
	public boolean isUseDefaultValue() {
		return useDefaultValue;
	}

	@Override
	public boolean isUseGenerator() {
		return useGenerator;
	}

	protected abstract Class<? extends Annotation> getAnnotationType();

}
