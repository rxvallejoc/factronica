/**
 * 
 */
package com.obiectumclaro.factronica.core.validator.compound;

import com.obiectumclaro.factronica.core.validator.ValidatorRequest;
import com.obiectumclaro.factronica.core.validator.ValidatorResponse;
import com.obiectumclaro.factronica.core.validator.simple.FixedLengthValidator;
import com.obiectumclaro.factronica.core.validator.simple.NumberValidator;

/**
 * @author iapazmino
 *
 */
public class RUCValidator extends CompoundValidator {

	/* (non-Javadoc)
	 * @see com.obiectumclaro.factronica.core.validator.compound.CompoundValidator#validate(java.lang.String)
	 */
	@Override
	public ValidatorResponse validate(final String ruc) {
		return calculateResponse(new FixedLengthValidator().validate(new ValidatorRequest<Integer>(13, ruc)),
				new NumberValidator().validate(new ValidatorRequest<Boolean>(true, ruc)));
	}

	/* (non-Javadoc)
	 * @see com.obiectumclaro.factronica.core.validator.compound.CompoundValidator#getMessage()
	 */
	@Override
	protected String getMessage() {
		return "El RUC debe ser un número de 13 dígitos";
	}

}
