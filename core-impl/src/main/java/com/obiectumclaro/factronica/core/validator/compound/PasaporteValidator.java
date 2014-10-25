/**
 * 
 */
package com.obiectumclaro.factronica.core.validator.compound;

import com.obiectumclaro.factronica.core.validator.Interval;
import com.obiectumclaro.factronica.core.validator.ValidatorRequest;
import com.obiectumclaro.factronica.core.validator.ValidatorResponse;
import com.obiectumclaro.factronica.core.validator.simple.VariableLengthValidator;

/**
 * @author iapazmino
 *
 */
public class PasaporteValidator extends CompoundValidator {

	/* (non-Javadoc)
	 * @see com.obiectumclaro.factronica.core.validator.compound.CompoundValidator#validate(java.lang.String)
	 */
	public ValidatorResponse validate(final String pasaporte) {
		final ValidatorRequest<Interval> request = new ValidatorRequest<Interval>(new Interval(3, 10), pasaporte);
		return calculateResponse(new VariableLengthValidator().validate(request));
	}

	@Override
	protected String getMessage() {
		return "El pasaporte es una cadena alfanumérica de mínimo 3 y máximo 10 caracteres";
	}

}
