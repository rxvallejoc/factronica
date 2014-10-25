/**
 * 
 */
package com.obiectumclaro.factronica.core.validator.simple;

import java.io.Serializable;

import com.obiectumclaro.factronica.core.validator.ValidatorRequest;
import com.obiectumclaro.factronica.core.validator.ValidatorResponse;

/**
 * Defines the contract for validation of {@link String}s.
 * 
 * @author iapazmino
 * 
 */
public interface SimpleValidator<T extends Serializable> {

	/**
	 * Public interface to request a validation.
	 * 
	 * @param request
	 * @return ValidatorResponse 
	 */
	ValidatorResponse validate(ValidatorRequest<T> request);

}
