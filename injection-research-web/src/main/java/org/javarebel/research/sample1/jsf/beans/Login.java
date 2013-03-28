package org.javarebel.research.sample1.jsf.beans;

import java.io.Serializable;

public class Login implements Serializable {

	private static final long serialVersionUID = -5392154904377629274L;

	private String username;
	private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return new StringBuilder("User Name : ").append(this.username)
				.append(" Password : ").append(this.password).toString();
	}
}
