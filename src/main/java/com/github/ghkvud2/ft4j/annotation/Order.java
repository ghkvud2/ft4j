package com.github.ghkvud2.ft4j.annotation;

import java.lang.annotation.*;

@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Order {

	int value();
}
