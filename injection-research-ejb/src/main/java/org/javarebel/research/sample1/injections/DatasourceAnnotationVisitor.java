package org.javarebel.research.sample1.injections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.interceptor.InvocationContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DatasourceAnnotationVisitor extends AnnotationVisitor {
	
	public DatasourceAnnotationVisitor(InvocationContext ctx) {
		super(ctx);
	}
	
	private void setValue(final Object target, final Field fld, final Object value) {
		boolean isFieldAccessible = false;
		System.out.println("Inside DBVisitor Before ==> " + target);
		System.out.println(String.format("Setting value for field %s in class %s with value %s", 
				fld.getName(), fld.getDeclaringClass().getCanonicalName(), value));
		try {
			isFieldAccessible = fld.isAccessible();
			if(!isFieldAccessible)
				fld.setAccessible(true);
			System.out.println("Before setting " + fld.get(target));
			if(Modifier.isStatic(fld.getModifiers()))
				fld.set(null, value);
			else {
				System.out.println("Setting field value");
				fld.set(target, value);
				System.out.println("After setting value of ds is " + fld.get(target));
				
				Field fld1 = target.getClass().getSuperclass().getDeclaredField("ds");
				fld1.setAccessible(true);
				fld1.set(target, value);
				fld1.setAccessible(false);
				System.out.println(fld.getDeclaringClass().getDeclaredMethod("getDS").invoke(target));
				
				System.out.println("After setting value of ds is ==> " + target);
			}
		} catch (SecurityException | IllegalArgumentException
				| IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			System.out.println( e.getMessage());
			throw new RuntimeException(e.getMessage(), e);
		} catch (NoSuchFieldException e) {
			System.out.println( e.getMessage());
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if(!isFieldAccessible)
				fld.setAccessible(false);
		}
		System.out.println("Inside DBVisitor after ==> " + target);
	}

	@Override
	public void visit(AnnotationHandler handler) {
		Object obj = ctx.getTarget();
		Class<?> parentClass = obj.getClass();
		System.out.println(String.format("Visitng Datasource Annotation for class %s", 
					parentClass.getCanonicalName()));
		
		List<Field> fields = new ArrayList<>();
		Context nmCtx = null;
		JNDIResource resource = null;
		String jndiName = null;
		Object dsObj = null;
		
		try {
			nmCtx = new InitialContext();
		} catch (NamingException e) {
			System.out.println( e.getMessage());
			throw new RuntimeException(e.getMessage(), e);
		}
		
		do {
			System.out.println("ParentClass == >" + parentClass);
			fields.addAll(Arrays.asList(parentClass.getDeclaredFields()));
			System.out.println("Fields ==> " + fields);
			parentClass = parentClass.getSuperclass();
		} while(parentClass != null);
		
		for(Field fld : fields) {
			if(fld.isAnnotationPresent(JNDIResource.class)) {
				resource = fld.getAnnotation(JNDIResource.class);
				jndiName = resource.value();
				if(fld.getType() == DataSource.class) {
					try {	
						dsObj = nmCtx.lookup(jndiName);
						System.out.println(String.format("JNDI lookup for name %s returns %s", jndiName, dsObj));
						if(dsObj != null) 
							setValue(obj, fld, dsObj);
					} catch (NamingException e) {
						System.out.println( e.getMessage());
						throw new RuntimeException(e.getMessage(), e);
					}
				}
			}
		}
	}
}
