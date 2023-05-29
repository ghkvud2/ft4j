package com.github.ghkvud2.ft4j.annotation;

import java.lang.annotation.*;

import com.github.ghkvud2.ft4j.generator.Generator;
import com.github.ghkvud2.ft4j.generator.NoopGenerator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GeneratedValue {

	Class<? extends Generator> generator() default NoopGenerator.class;

	boolean cacheable() default false;
	
	String key() default "";
	
}
