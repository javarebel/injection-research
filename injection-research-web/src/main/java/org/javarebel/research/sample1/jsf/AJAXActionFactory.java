package org.javarebel.research.sample1.jsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class AJAXActionFactory {

	@Inject
	@Any
	private Instance<Action> ajaxActions;
	
	@Produces
	public List<String> getAjaxActionNames() {
		List<String> ajaxList = new ArrayList<>();
		for(Action ajax : ajaxActions) {
			ajaxList.add(ajax.getClass().getAnnotation(AJAXEnabled.class).value());
		}
		return ajaxList;
	}
	
	@Produces
	@AJAXCache
	public Map<String, Action> getAJAXActionMap() {
		Map<String, Action> map = new HashMap<>();
		String key = null;
		Class<?> anyClass = null;
		for(Action ajax : ajaxActions) {
			key = ajax.getClass().getAnnotation(AJAXEnabled.class).value();
			anyClass = ajax.getClass();
			if(Action.class.isAssignableFrom(anyClass)) {
				try {
					Object actObj = anyClass.newInstance();
					map.put(key, Action.class.cast(actObj));
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
				
			}
		}
		return map;
	}
}
