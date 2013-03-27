package org.javarebel.research.sample1.jsf;

import javax.enterprise.util.AnnotationLiteral;

@SuppressWarnings("serial")
public class AJAXEnabledQualifier extends AnnotationLiteral<AJAXEnabled> implements AJAXEnabled {
	final String expectedName;

	public AJAXEnabledQualifier(String expectedName) {
        this.expectedName = expectedName;
    }
    
	@Override
	public String value() {
		return expectedName;
	}
}
