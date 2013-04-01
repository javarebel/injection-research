/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javarebel.research.commons.dao;

import java.lang.reflect.Field;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author naveen
 */
public class CommonDAOFactory {
    
    @SuppressWarnings("unchecked")
	public static <T> T getDAO(InjectionPoint ip) {
        Field fld = (Field)ip.getMember();
        Class<?> daoClass = fld.getType();
        Object obj = DAOProxyfier.getInstance(daoClass);
        return (T)obj;
    }
}
