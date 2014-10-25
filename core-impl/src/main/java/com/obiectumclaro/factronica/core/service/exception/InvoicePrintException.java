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
public class InvoicePrintException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvoicePrintException(String message) {
		super(message);
	}

	public InvoicePrintException(Throwable t) {
		super(t);
	}

	public InvoicePrintException(String message, Throwable t) {
		super(message, t);
	}
}
