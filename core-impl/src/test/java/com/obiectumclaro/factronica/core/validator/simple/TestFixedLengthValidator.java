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
import com.obiectumclaro.factronica.core.validator.simple.FixedLengthValidator;
import com.obiectumclaro.factronica.core.validator.simple.SimpleValidator;

/**
 * Test cases to verify the {@link FixedLengthValidator}.
 *  
 * @author iapazmino
 *
 */
public class TestFixedLengthValidator {

	private static final int EXPECTED = 3;
	
	private SimpleValidator<Integer> flv;
	private ValidatorRequest<Integer> request;
	
	@Before
	public void setupValidator() {
		flv = new FixedLengthValidator();
		request = new ValidatorRequest<Integer>();
		request.setExpected(EXPECTED);
	}

	@Test
	public void shouldAccept() {
		request.setActual("123");
		final ValidatorResponse resp = flv.validate(request);
		assertTrue("Response was <invalid>", resp.isValid());
	}
	
	@Test
	public void shoulNotAccept() {
		request.setActual("1234");
		final ValidatorResponse resp = flv.validate(request);
		assertFalse("Response was <valid>", resp.isValid());
	}
	
	@Test
	public void shoulAddMessage() {
		request.setActual("1234");
		final ValidatorResponse resp = flv.validate(request);
		assertEquals("Se esperaba una longitud de <3> pero en su lugar se obtuvo <4>", resp.getMessage());
	}
	
}
