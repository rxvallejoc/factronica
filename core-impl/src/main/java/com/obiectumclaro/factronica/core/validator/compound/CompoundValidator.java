/**
 * 
 */
package com.obiectumclaro.factronica.core.validator.compound;

import com.obiectumclaro.factronica.core.validator.ValidatorResponse;

/**
 * A validator which delegates actual validation to simple validators.
 * 
 * @author iapazmino
 *
 */
public abstract class CompoundValidator {

	public abstract ValidatorResponse validate(String subject);
	
	protected ValidatorResponse calculateResponse(final ValidatorResponse... responses) {
		final ValidatorResponse response = new ValidatorResponse(true);
		for (ValidatorResponse resp : responses) {
			if (!resp.isValid()) {
				response.setValid(false);
				response.setMessage(getMessage());
				return response;
			}
		}
		return response;
	}
	
	protected abstract String getMessage();

}
