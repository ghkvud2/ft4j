package com.github.ghkvud2.ft4j.annotation;

import java.lang.annotation.*;

import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DoubleValue {

	int order();
	
	int length();

	String defaultValue() default "";

	int fractionalLength() default -1;

	PaddingByte paddingByte() default PaddingByte.ZERO;

	Justify justify() default Justify.RIGHT;

	boolean ignoreLimit() default false;

}
