package org.javarebel.research.sample1.injections;

public class AnnotationHandlerImpl implements AnnotationHandler {

	@Override
	public void accept(AnnotationVisitor visitor) {
		visitor.visit(this);
	}
}
