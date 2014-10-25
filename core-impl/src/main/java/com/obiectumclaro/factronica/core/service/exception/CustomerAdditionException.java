/**
 * 
 */
package com.obiectumclaro.factronica.core.service.exception;

import com.obiectumclaro.factronica.core.model.Customer;

/**
 * Exception which denotes a problem while adding a new {@link Customer}.
 * 
 * @author iapazmino
 * 
 */
public class CustomerAdditionException extends Exception {

	private static final long serialVersionUID = 1L;

	public CustomerAdditionException(final String message) {
		super(message);
	}

}
