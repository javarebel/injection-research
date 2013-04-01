package org.javarebel.research.commons.dao;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author naveen
 */
public class AirFrameworkDAOException extends Exception {

	private static final long serialVersionUID = 24302573161146430L;

	public AirFrameworkDAOException() {
    }

    public AirFrameworkDAOException(String message) {
        super(message);
    }

    public AirFrameworkDAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public AirFrameworkDAOException(Throwable cause) {
        super(cause);
    }

    public AirFrameworkDAOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
