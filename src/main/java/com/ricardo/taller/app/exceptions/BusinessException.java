package com.ricardo.taller.app.exceptions;

/**
 * 
 * @author ricardo
 *
 */
public class BusinessException extends Exception {
	
	private static final long serialVersionUID = -1924476522408445506L;
	
	public BusinessException(String mensaje ) {
		super(mensaje);
	}
	
}
