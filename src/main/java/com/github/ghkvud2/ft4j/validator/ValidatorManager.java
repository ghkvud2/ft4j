package com.github.ghkvud2.ft4j.validator;

import com.github.ghkvud2.ft4j.property.AnnotationFieldProperty;

public interface ValidatorManager {

	void preValidate(AnnotationFieldProperty property);

	void postValidate(AnnotationFieldProperty property, byte[] bytes);
}
