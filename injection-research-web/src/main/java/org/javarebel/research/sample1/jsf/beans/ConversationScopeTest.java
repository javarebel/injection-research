package org.javarebel.research.sample1.jsf.beans;

import java.io.Serializable;
import javax.ejb.EJB;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import org.javarebel.research.sample1.ejb.EmployeeManager;
import org.javarebel.research.sample1.entity.LoginCredentials;
import org.javarebel.research.sample1.entity.UserInfo;

@Named("convTest")
@ConversationScoped
public class ConversationScopeTest implements Serializable {

	private static final long serialVersionUID = 8458740725232792416L;

	@Produces
	@Named("login")
	private LoginCredentials login = new LoginCredentials();
	
	@Inject
	private Conversation conversation;
        
        @EJB
        private EmployeeManager empMgr;
	
	public String saveLogin() {
		conversation.begin();
		System.out.println("Login Information : \n" + login);
                login.setUserInfo(new UserInfo());
		return "userInfo.xhtml";
	}
	
	public String saveUser() {
		System.out.println("User Information : \n" + login.getUserInfo());
		return "confirm.xhtml";
	}
	
	public String createAccount() {
		System.out.println("Creating account.");
                empMgr.registerUser(login);
		conversation.end();
		return "result.xhtml";
	}
}
