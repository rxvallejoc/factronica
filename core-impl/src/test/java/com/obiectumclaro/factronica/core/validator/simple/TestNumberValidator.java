/**
 * 
 */
package com.obiectumclaro.factronica.core.validator.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.obiectumclaro.factronica.core.validator.ValidatorRequest;
import com.obiectumclaro.factronica.core.validator.ValidatorResponse;
import com.obiectumclaro.factronica.core.validator.simple.NumberValidator;
import com.obiectumclaro.factronica.core.validator.simple.SimpleValidator;

/**
 * Test cases to verify {@link NumberValidator}.
 * 
 * @author iapazmino
 *
 */
public class TestNumberValidator {

	private SimpleValidator<Boolean> nv;
	private ValidatorRequest<Boolean> request;
	
	@Before
	public void setup() {
		nv = new NumberValidator();
		request = new ValidatorRequest<Boolean>();
	}
	
	@Test
	public void shouldAccept() {
		request.setActual("123");
		final ValidatorResponse resp = nv.validate(request);
		assertTrue("Should have accepted it as a number", resp.isValid());
	}
	
	@Test
	public void shouldNotAccept() {
		request.setActual("123A");
		final ValidatorResponse response = nv.validate(request);
		assertFalse("Shouln'd have accepted an alphanumeric string", response.isValid());
	}
	
	@Test
	public void shouldAddMessage() {
		request.setActual("123A");
		final ValidatorResponse response = nv.validate(request);
		assertEquals("Se esperaba un número y se obtuvo una cadena alfanumérica <123A>", response.getMessage());
	}
	
}
