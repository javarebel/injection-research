package org.javarebel.research.sample1.jsf;

import javax.faces.context.FacesContext;

public interface Action {
	Object process(FacesContext ctx);
}
