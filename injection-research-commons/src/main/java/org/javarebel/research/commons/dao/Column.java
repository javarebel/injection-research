package org.javarebel.research.commons.dao;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to override column properties.
 * 
 * @author <a href='mailto:nsisupalan@prolifics.com'>Naveen Sisupalan</a> 
 * @version 1.1
 * @since 1.1
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
	String name() default "";
	Class<? extends Object> type() default Object.class;
	String defaultValue() default "";
	Class<? extends Serializable> defaultType() default Serializable.class;
}
