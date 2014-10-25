/**
 * 
 */
package com.obiectumclaro.factronica.core.service.exception;


/**
 * Exception which denotes a problem while generating an Agreement Document.
 * 
 * @author faustodelatog
 *
 */
public class AgreementException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AgreementException(String message){
		super(message);
	}
	
	public AgreementException(Throwable t){
		super(t);
	}
	
	public AgreementException(String message, Throwable t){
		super(message, t);
	}
}
