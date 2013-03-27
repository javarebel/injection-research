package org.javarebel.research.sample1.injections.test;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.validation.constraints.Size;

import org.javarebel.research.sample1.dao.Database;
import org.javarebel.research.sample1.injections.JNDIResource;
import org.javarebel.research.sample1.injections.Managed;

@Managed
public class EmployeeDAO extends AbstractDAO {
	
	@JNDIResource("jdbc/SOAAFCU")
	private DataSource ds;
	
	@Inject
	@Database("jdbc/SOAAFCU")
	private DataSource fds;
	
	@Inject
	private Logger logger;
	
	public EmployeeDAO() {
		System.out.println("FirstLevelChild construtor invoked===============>");
	}
	
	public DataSource getDS() {
		return this.ds;
	}
	
	public String toString() {
		System.out.println("Logger injected is " + logger);
		System.out.println("Factory injected datasource is " + fds);
		return new StringBuilder(super.toString()).append(" : DS ").append(this.ds).toString();
	}
	
	//Here Size validation does not work
	public boolean addEmployee(@Size(min=7,max=10,message="First Name length validation failed") String fname, String lname) {
		System.out.println(String.format("Adding new employee. First Name %s and Last Name %s", fname, lname));
		return true;
	}
}
