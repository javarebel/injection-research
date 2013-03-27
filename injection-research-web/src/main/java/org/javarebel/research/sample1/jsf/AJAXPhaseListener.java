package org.javarebel.research.sample1.jsf;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@SuppressWarnings("serial")
public class AJAXPhaseListener implements PhaseListener {
	
	private Map<String, Object> ajaxCache;

	@Override
	public void afterPhase(PhaseEvent arg0) {

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void beforePhase(PhaseEvent arg0) {
		try {
			Context ctx = new InitialContext();
			BeanManager beanMgr = (BeanManager) ctx.lookup("java:comp/BeanManager");
			System.out.println("Bean Manager is " + beanMgr);
			
			Set<Bean<?>> beans = beanMgr.getBeans(AJAXActionFactory.class);
			System.out.println("Beans Size is " + beans.size());
			Iterator<Bean<?>> iter = beans.iterator();
			while(iter.hasNext()) {
				Bean<?> bean = iter.next();
				System.out.println(bean.getBeanClass().getSimpleName());
				CreationalContext<?> cc = beanMgr.createCreationalContext(bean);
				AJAXActionFactory fac = (AJAXActionFactory)beanMgr.getReference(bean, AJAXActionFactory.class, cc);
				
				System.out.println(fac.getAjaxActionNames());
			}
			
			// If you want an action class which is annotated with a particular annotation.
			// Then create a class which extends AnnotationLiteral<Annotation>
			//Set<Bean<?>> actBeans = beanMgr.getBeans(Action.class, new AJAXEnabledQualifier("empManager"));
			
			// If you want Action classes which is annotated with any kind of annotation
			Set<Bean<?>> actBeans = beanMgr.getBeans(Action.class, new AnnotationLiteral<Any>() {});
			System.out.println("Action Beans Size is " + actBeans.size());
			Iterator<Bean<?>> actIter = actBeans.iterator();
			while(actIter.hasNext()) {
				Bean<?> bean = actIter.next();
				System.out.println(bean.getBeanClass().getSimpleName());
				CreationalContext<?> cc = beanMgr.createCreationalContext(bean);
				Action act = (Action)beanMgr.getReference(bean, Action.class, cc);
				
				System.out.println(act.getClass().getName());
			}
			
			Set<Bean<?>> cacheBeans = beanMgr.getBeans(Map.class, new AnnotationLiteral<AJAXCache>() {});
			System.out.println("Cache Beans Size is " + cacheBeans.size());
			Iterator<Bean<?>> cacheIter = cacheBeans.iterator();
			while(cacheIter.hasNext()) {
				Bean<?> bean = cacheIter.next();
				System.out.println(bean.getBeanClass().getSimpleName());
				CreationalContext<?> cc = beanMgr.createCreationalContext(bean);
				Map cacheMap = (Map)beanMgr.getReference(bean, Map.class, cc);
				
				System.out.println(cacheMap);
			}
			
			System.out.println("AJAX Cache in PhaseListener is " + this.ajaxCache);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
