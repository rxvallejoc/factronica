/**
 * 
 */
package com.obiectumclaro.factronica.core.service.exception;


/**
 * Exception which denotes a problem while adding a new {@link Product}.
 * 
 * @author faustodelatog
 *
 */
public class ProductAdditionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProductAdditionException(String message){
		super(message);
	}
}
