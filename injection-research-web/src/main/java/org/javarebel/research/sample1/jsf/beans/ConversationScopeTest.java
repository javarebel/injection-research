package org.javarebel.research.sample1.jsf.beans;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

@Named("convTest")
@ConversationScoped
public class ConversationScopeTest implements Serializable {

	private static final long serialVersionUID = 8458740725232792416L;

	@Produces
	@Named
	public User user = new User();
	
	@Produces
	@Named
	public Login login = new Login();
	
	@Inject
	private Conversation conversation;
	
	public String saveLogin() {
		conversation.begin();
		System.out.println("Login Information : \n" + login);
		return "userInfo.faces";
	}
	
	public String saveUser() {
		System.out.println("User Information : \n" + user);
		return "confirm.faces";
	}
	
	public String createAccount() {
		System.out.println("Creating account.");
		conversation.end();
		return "result.faces";
	}
}