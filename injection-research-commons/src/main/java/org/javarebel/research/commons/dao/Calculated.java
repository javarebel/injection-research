package org.javarebel.research.commons.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark filed in VO class which needs calculation when using with AirFrameworkDBUtil
 * 
 * @author <a href='mailto:nsisupalan@prolifics.com'>Naveen Sisupalan</a> 
 * @version 1.0
 * @since 1.2.1
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Calculated {
	String expr();
}
