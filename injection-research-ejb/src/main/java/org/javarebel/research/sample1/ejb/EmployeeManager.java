package org.javarebel.research.sample1.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.javarebel.research.commons.dao.AirFrameworkDAOException;
import org.javarebel.research.commons.dao.DAO;
import org.javarebel.research.sample1.dao.EmployeeDAOManager;
import org.javarebel.research.sample1.dao.IBranchDAO;
import org.javarebel.research.sample1.dao.IEmployeeDAO;
import org.javarebel.research.sample1.entity.LoginCredentials;

import org.javarebel.research.sample1.injections.test.EmployeeDAO;

/**
 * Session Bean implementation class EmployeeManager
 */
@Stateless
@LocalBean
public class EmployeeManager {
	
    @Inject
    private EmployeeDAO child;
    
    @Inject
    private EmployeeDAOManager empMgr;
    
    @Inject
    @DAO
    private IEmployeeDAO empDAO;
    
    @Inject @DAO
    private IBranchDAO branchDAO;
    
    @Inject
    private IEmployeeDAO eDAO;
    
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
        
        try {
            System.out.println(" empDAO ==>> " + empDAO.findEmplyeesById(2060L));
        } catch (AirFrameworkDAOException ex) {
            ex.printStackTrace();
        }
        
        try {
            System.out.println(" branchDAO ==>> " + branchDAO.findBranchById(88L));
        } catch (AirFrameworkDAOException ex) {
            ex.printStackTrace();
        }
        
       try {
           System.out.println("eDAO ==> " + eDAO.getClass().getSimpleName());
           System.out.println(" eDAO ==>> " + eDAO.findEmplyeesById(1L));
        } catch (AirFrameworkDAOException ex) {
            ex.printStackTrace();
        }
        
        
        System.out.println(" empDAO ==>> " + empDAO);
    	return true;
    }
    
    public boolean registerUser(LoginCredentials login) {
        empMgr.registerUser(login);
        return true;
    }
}
