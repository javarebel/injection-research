package org.javarebel.research.sample1.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.javarebel.research.sample1.injections.test.EmployeeDAO;

/**
 * Session Bean implementation class EmployeeManager
 */
@Stateless
@LocalBean
public class EmployeeManager {
	
	@Inject
	private EmployeeDAO child;

    /**
     * Default constructor. 
     */
    public EmployeeManager() {
    }

    public boolean addEmployee(String fname, String lname) {
    	System.out.println("<=============  Inside EJB ===========================>");
    	System.out.println("DS Injected ==> " + child.getDS());
    	System.out.println("<============= EJB Finished ===========================>");
    	child.addEmployee(fname, lname);
    	return true;
    }
}
