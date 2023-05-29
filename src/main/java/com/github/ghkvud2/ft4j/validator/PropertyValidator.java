package com.github.ghkvud2.ft4j.validator;

import com.github.ghkvud2.ft4j.property.AnnotationFieldProperty;

public interface PropertyValidator {

	boolean support(AnnotationFieldProperty property);

	boolean isValidType(AnnotationFieldProperty property);
	

}