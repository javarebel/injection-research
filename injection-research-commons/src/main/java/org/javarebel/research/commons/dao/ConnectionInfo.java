package org.javarebel.research.commons.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConnectionInfo {

	String url() default Const.NULL;
	String user() default Const.NULL;
	String password() default Const.NULL;
	String datasource() default Const.NULL;
	String value() default Const.NULL;
}

abstract class Const {
	public static final String NULL = "NULL";
}
