/**
 * 
 */
package com.obiectumclaro.factronica.core.validator.simple;

import com.obiectumclaro.factronica.core.validator.ValidatorRequest;
import com.obiectumclaro.factronica.core.validator.ValidatorResponse;

/**
 * Verifies if a {@link String} can be parsed to a number.
 * 
 * @author iapazmino
 *
 */
public class NumberValidator implements SimpleValidator<Boolean> {
	
	private static final String MESSAGE = "Se esperaba un número y se obtuvo una cadena alfanumérica <%s>";

	/* (non-Javadoc)
	 * @see com.obiectumclaro.factronica.core.validator.simple.SimpleValidator#validate(com.obiectumclaro.factronica.core.validator.ValidatorRequest)
	 */
	public ValidatorResponse validate(final ValidatorRequest<Boolean> request) {
		final ValidatorResponse response = new ValidatorResponse();
		try {
			Long.parseLong(request.getActual());
			response.setValid(true);
		} catch (NumberFormatException nfe) {
			response.setValid(false);
			response.setMessage(String.format(MESSAGE, request.getActual()));
		}
		return response;
	}

}
