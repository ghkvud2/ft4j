package com.github.ghkvud2.ft4j.annotation;

import java.lang.annotation.*;

import com.github.ghkvud2.ft4j.annotation.constant.Justify;
import com.github.ghkvud2.ft4j.annotation.constant.PaddingByte;

@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Message {

	int length();

	String defaultValue() default "";

	PaddingByte paddingByte() default PaddingByte.SPACE;

	Justify justify() default Justify.LEFT;
}
