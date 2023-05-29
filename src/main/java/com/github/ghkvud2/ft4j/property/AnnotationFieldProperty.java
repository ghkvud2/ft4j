package com.github.ghkvud2.ft4j.property;

import java.lang.reflect.Field;

import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;

public interface AnnotationFieldProperty {

	// 클래스 필드
	Field getField();

	// 어노테이션 기본 속성
	int length();

	String defaultValue();

	boolean isUseDefaultValue();

	PaddingByte padding();

	Justify justify();

	int fractionalLength();

	// 추가 메소드
	Object getFieldValue();

	String convertToString();

	boolean ignoreLimit();

	// Generator
	boolean isUseGenerator();
	
	//필드 값 세팅
	void setFieldValueFromString(String value);

	void setFieldValueFromObject();
	
	void setFieldValueFromBytes(byte[] bytes);

}
