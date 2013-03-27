package org.javarebel.research.sample1.injections;

import javax.interceptor.InvocationContext;

public abstract class AnnotationVisitor {
	protected InvocationContext ctx;
	AnnotationVisitor(InvocationContext ctx) {
		this.ctx = ctx;
	}
	public abstract void visit(AnnotationHandler handler);
}	
