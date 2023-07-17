package com.github.ghkvud2.ft4j.annotation;

import java.lang.annotation.*;

import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface StringValue {

	int order();
	
	int length();

	String defaultValue() default "";

	PaddingByte paddingByte() default PaddingByte.SPACE;

	Justify justify() default Justify.LEFT;
}
