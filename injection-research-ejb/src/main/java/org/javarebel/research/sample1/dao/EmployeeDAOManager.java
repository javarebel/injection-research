/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javarebel.research.sample1.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.javarebel.research.sample1.entity.LoginCredentials;


/**
 *
 * @author naveen
 */
public class EmployeeDAOManager {
    
    @PersistenceContext
    private EntityManager mgr;
    
    public boolean registerUser(LoginCredentials login) {
        System.out.println("Entity Manager is " + mgr);
        mgr.persist(login);
        return true;
    }
}
