/**
 * 
 */
package com.obiectumclaro.factronica.core.validator.compound;

import com.obiectumclaro.factronica.core.validator.ValidatorRequest;
import com.obiectumclaro.factronica.core.validator.ValidatorResponse;
import com.obiectumclaro.factronica.core.validator.simple.FixedLengthValidator;
import com.obiectumclaro.factronica.core.validator.simple.NumberValidator;
import com.obiectumclaro.factronica.core.validator.simple.SimpleValidator;

/**
 * Verifies if a cedula is valid by delegating length and is-a-number validations 
 * to the respective simple validators.
 * 
 * @author iapazmino
 *
 */
public class CedulaValidator extends CompoundValidator {
	
	private static final int CEDULA_LENGTH = 10;

	public ValidatorResponse validate(final String cedula) {
		final SimpleValidator<Integer> lengthVal = new FixedLengthValidator();
		final SimpleValidator<Boolean> numberVal = new NumberValidator();
		final ValidatorResponse respLength = lengthVal.validate(new ValidatorRequest<Integer>(CEDULA_LENGTH, cedula));
		final ValidatorResponse respNumber = numberVal.validate(new ValidatorRequest<Boolean>(true, cedula));
		return calculateResponse(respLength, respNumber);
	}

	@Override
	protected String getMessage() {
		return "La cédula debe ser un número de diez dígitos";
	}

}
