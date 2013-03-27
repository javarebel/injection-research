package org.javarebel.research.sample1.dao;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DatasourceFactory {

	@Produces
	@Database
	public DataSource getDS(InjectionPoint ip) {
		Database dbAnn = ip.getAnnotated().getAnnotation(Database.class);
		String jndi = dbAnn.value();
		DataSource ds = null;
		try {
			Context ctx = new InitialContext();
			Object obj = ctx.lookup(jndi);
			if(obj != null && obj instanceof DataSource)
				ds = DataSource.class.cast(obj);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return ds;
	}
	
	@Produces
	public Logger getLogger(InjectionPoint ip) {
		return Logger.getLogger(ip.getMember().getDeclaringClass().getSimpleName());
	}
}
