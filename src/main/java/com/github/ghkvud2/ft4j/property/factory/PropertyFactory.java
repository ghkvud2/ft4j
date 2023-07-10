package com.github.ghkvud2.ft4j.property.factory;

import java.lang.reflect.Field;

import com.github.ghkvud2.ft4j.property.AnnotationFieldProperty;

public interface PropertyFactory {

	AnnotationFieldProperty createProperty(Object obj, Field field);

}
