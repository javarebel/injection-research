package org.javarebel.research.sample1.injections;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@InterceptorBinding
public @interface Managed {

}
