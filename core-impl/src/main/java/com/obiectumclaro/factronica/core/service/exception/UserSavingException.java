/**
 * 
 */
package com.obiectumclaro.factronica.core.service.exception;

/**
 * 
 * @author faustodelatog
 * 
 */
public class UserSavingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserSavingException(String message) {
		super(message);
	}

	public UserSavingException(Throwable t) {
		super(t);
	}

	public UserSavingException(String message, Throwable t) {
		super(message, t);
	}
}
