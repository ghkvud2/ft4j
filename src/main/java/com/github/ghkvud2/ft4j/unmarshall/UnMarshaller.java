package com.github.ghkvud2.ft4j.unmarshall;

import com.github.ghkvud2.ft4j.property.AnnotationFieldProperty;

public interface UnMarshaller {

	boolean supports(AnnotationFieldProperty property);
	
	int unmarshall(AnnotationFieldProperty property, int offset, byte[] bytes);
	
	byte[] convertToBytes(String input);
}
