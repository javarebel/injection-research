package org.javarebel.research.sample1.jsf.beans;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = -7288857913671670724L;

	private String fname;
	private String lname;
	private String address;
	private String phone;
	
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	public String toString() {
		return new StringBuilder("FName : ").append(this.fname).append(" LName : :")
				.append(this.lname).append(" Address : ").append(this.address)
				.append(" Phone : ").append(this.phone).toString();
	}
}
