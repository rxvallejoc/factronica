/**
 * 
 */
package com.obiectumclaro.factronica.core.validator;

import java.io.Serializable;

/**
 * @author iapazmino
 * 
 */
public class ValidatorRequest<T extends Serializable> {

	private T expected;
	private String actual;

	/**
	 * Constructs an instance with both expected and actual values set to null.
	 */
	public ValidatorRequest() {
		this(null, null);
	}

	/**
	 * Constructs an instance initializing both expected and actual values.
	 * 
	 * @param expected
	 *            is the value you expect to match to.
	 * @param actual
	 *            is the string you want to validate against.
	 */
	public ValidatorRequest(final T expected, final String actual) {
		this.expected = expected;
		this.actual = actual;
	}

	public T getExpected() {
		return expected;
	}

	public void setExpected(T expected) {
		this.expected = expected;
	}

	public String getActual() {
		return actual;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}
}
