/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javarebel.research.sample1.dao;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import org.javarebel.research.commons.dao.CommonDAOFactory;
import org.javarebel.research.commons.dao.DAO;

/**
 *
 * @author naveen
 */
public class DAOFactory {
    
    @Produces
    @DAO
    public IEmployeeDAO getEmployeeDAO(InjectionPoint ip) {
        return CommonDAOFactory.getDAO(ip);
    }
    
    @Produces
    @DAO
    public IBranchDAO getBranchDAO(InjectionPoint ip) {
        return CommonDAOFactory.getDAO(ip);
    }
}
