package org.javarebel.research.sample1.injections;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Managed
@Interceptor
public class InjectionInterceptor {
	
	@Inject
	private AnnotationHandler handler;
	
	@PostConstruct
	public void injectResource(InvocationContext ctx) {
		System.out.println("Before Inj ==> " + ctx.getTarget());
		Class<?> targetClass = ctx.getTarget().getClass();
		do {
			if(targetClass.isAnnotationPresent(Managed.class)) {
				System.out.println(String.format("Injection Required for class %s", targetClass.getCanonicalName()));
				this.handler.accept(new DatasourceAnnotationVisitor(ctx));
				break;
			}
			targetClass = targetClass.getSuperclass();
		} while(targetClass != null);
		System.out.println("After injection ==> " + ctx.getTarget());
	}
}
