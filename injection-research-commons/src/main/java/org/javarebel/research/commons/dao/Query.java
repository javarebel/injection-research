package org.javarebel.research.commons.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Query {

	String value();
	enum Type {DIRECT, INDIRECT}
	Type queryType() default Type.DIRECT;
}


