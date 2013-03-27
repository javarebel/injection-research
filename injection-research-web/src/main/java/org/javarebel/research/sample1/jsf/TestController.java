package org.javarebel.research.sample1.jsf;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.Size;

import org.javarebel.research.sample1.ejb.EmployeeManager;

@RequestScoped
@Named("test")
@SuppressWarnings("serial")
public class TestController implements Serializable {
	
	private static final Logger logger = Logger.getLogger(TestController.class.getName());

	@Size(min=5, max=10, message="Length must be >= 5 <= 10")
	private String fname;
	private String lname;
	private String message;
	
	@EJB
	private EmployeeManager empMgr;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	
	public void saveEmployee() {
		String message = String.format("First Name : %s and Last Name : %s", this.fname, this.lname);
		logger.info(message);
		System.out.println(message);
		empMgr.addEmployee(this.fname, this.lname);
	}
}
