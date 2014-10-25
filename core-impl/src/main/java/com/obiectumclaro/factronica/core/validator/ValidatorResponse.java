/**
 * 
 */
package com.obiectumclaro.factronica.core.validator;

/**
 * Object used to carry the response to a validation request.
 * 
 * @author iapazmino
 * 
 */
public class ValidatorResponse {

	private boolean valid;
	private String message;

	/**
	 * Constructs an instance with and empty message and set valid flag to
	 * false.
	 */
	public ValidatorResponse() {
		this(false);
	}

	/**
	 * Constructs an instance setting the valid value.
	 * 
	 * @param valid
	 *            flag to determine if the validation was passed.
	 */
	public ValidatorResponse(final boolean valid) {
		this.valid = valid;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

}
