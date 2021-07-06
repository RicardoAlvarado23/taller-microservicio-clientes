package com.ricardo.taller.app.exceptions;

/**
 * 
 * @author ricardo
 *
 */
public class RequiredException extends Exception {

	private static final long serialVersionUID = -246227741420058562L;

	public RequiredException(String mensaje) {
		super(mensaje);
	}
}
