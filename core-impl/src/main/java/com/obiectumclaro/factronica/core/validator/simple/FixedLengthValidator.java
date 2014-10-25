/**
 * 
 */
package com.obiectumclaro.factronica.core.validator.simple;

import com.obiectumclaro.factronica.core.validator.ValidatorRequest;
import com.obiectumclaro.factronica.core.validator.ValidatorResponse;

/**
 * Validates a string is of the expected length.
 * 
 * @author iapazmino
 *
 */
public class FixedLengthValidator implements SimpleValidator<Integer> {
	
	private static final String MESSAGE = "Se esperaba una longitud de <%s> pero en su lugar se obtuvo <%s>";

	public ValidatorResponse validate(final ValidatorRequest<Integer> request) {
		final ValidatorResponse response = new ValidatorResponse();
		final int length = request.getActual().length();
		final int expected = request.getExpected();
		response.setValid(expected == length);
		response.setMessage(String.format(MESSAGE, expected, length));
		return response;
	}

}
