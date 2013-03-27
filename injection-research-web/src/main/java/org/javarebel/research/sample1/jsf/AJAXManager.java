package org.javarebel.research.sample1.jsf;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named("ajax")
public class AJAXManager {
	
	@Inject
	@AJAXCache
	private Map<String, Object> ajaxCache;
	
	private String selectedAction;
	
	@Inject
	private List<String> actionNames;

	public List<String> getActionNames() {
		return actionNames;
	}

	public void setActionNames(List<String> actionNames) {
		this.actionNames = actionNames;
	}

	public String getSelectedAction() {
		return selectedAction;
	}

	public void setSelectedAction(String selectedAction) {
		this.selectedAction = selectedAction;
	}
	
	public void processAction() {
		System.out.println("Action Cache is " + ajaxCache);
	}
	
}
