package com.github.ghkvud2.ft4j.property.factory;

import java.lang.reflect.Field;

import com.github.ghkvud2.ft4j.property.AnnotationFieldProperty;

public interface PropertyFactory {

	AnnotationFieldProperty createMarshallProperty(Object obj, Field field);

	AnnotationFieldProperty createUnmarshallProperty(Object obj, Field field);

}
