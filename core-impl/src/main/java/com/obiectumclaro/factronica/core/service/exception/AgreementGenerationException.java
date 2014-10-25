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
public class AgreementGenerationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AgreementGenerationException(String message){
		super(message);
	}
	
	public AgreementGenerationException(Throwable t){
		super(t);
	}
	
	public AgreementGenerationException(String message, Throwable t){
		super(message, t);
	}
}
