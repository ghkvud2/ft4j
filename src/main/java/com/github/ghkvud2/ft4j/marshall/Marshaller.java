package com.github.ghkvud2.ft4j.marshall;

import com.github.ghkvud2.ft4j.property.AnnotationFieldProperty;

public interface Marshaller {

	boolean supports(AnnotationFieldProperty property);

	String marshall(AnnotationFieldProperty property);

	String convertWithCharset(String input);
}
