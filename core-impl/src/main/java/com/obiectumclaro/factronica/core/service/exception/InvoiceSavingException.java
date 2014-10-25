/**
 * 
 */
package com.obiectumclaro.factronica.core.service.exception;

/**
 * Exception which denotes a problem while printing an Invoice.
 * 
 * @author faustodelatog
 * 
 */
public class InvoiceSavingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvoiceSavingException(String message) {
		super(message);
	}

	public InvoiceSavingException(Throwable t) {
		super(t);
	}

	public InvoiceSavingException(String message, Throwable t) {
		super(message, t);
	}
}
