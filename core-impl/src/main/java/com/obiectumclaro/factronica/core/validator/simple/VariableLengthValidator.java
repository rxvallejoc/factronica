/**
 * 
 */
package com.obiectumclaro.factronica.core.validator.simple;

import com.obiectumclaro.factronica.core.validator.Interval;
import com.obiectumclaro.factronica.core.validator.ValidatorRequest;
import com.obiectumclaro.factronica.core.validator.ValidatorResponse;

/**
 * Verifies if a string's length is between a given interval. The validator takes
 * into account if the interval includes or excludes its endpoints.
 * 
 * @author iapazmino
 * 
 */
public class VariableLengthValidator implements SimpleValidator<Interval> {
	
	private static final String MESSAGE = "Se esperaba una longitud entre <%s> y <%s> %s pero en su lugar se obtuvo <%s>";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.obiectumclaro.factronica.core.validator.simple.SimpleValidator#validate
	 * (int, java.lang.String)
	 */
	public ValidatorResponse validate(final ValidatorRequest<Interval> request) {
		final ValidatorResponse response = new ValidatorResponse();
		final int length = request.getActual().length();
		final Interval expected = request.getExpected();
		final boolean includeEndpoint = expected.isInclusive();
		final boolean isOverMin = isOverMin(expected.getMin(), length, includeEndpoint);
		final boolean isUnderMax = isUnderMax(expected.getMax(), length, includeEndpoint);
		final boolean isValid = isOverMin && isUnderMax;
		response.setValid(isValid);
		if (!isValid) {
			final String clusive = includeEndpoint ? "inclusiva" : "exclusiva";
			response.setMessage(String.format(MESSAGE, expected.getMin(), expected.getMax(), clusive, length));
		}
		return response;
	}
	
	private boolean isUnderMax(final int expected, final int actual, final boolean includeEndpoint) {
		if (includeEndpoint) {
			return actual <= expected;
		} else {
			return actual < expected;
		}
	}
	
	private boolean isOverMin(final int expected, final int actual, final boolean includeEndpoint) {
		if (includeEndpoint) {
			return actual >= expected;
		} else {
			return actual > expected;
		}
	}

}
