/**
 * 
 */
package com.obiectumclaro.factronica.core.validator.compound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.obiectumclaro.factronica.core.validator.ValidatorResponse;

/**
 * @author iapazmino
 *
 */
public class TestCedulaValidator {

	private CompoundValidator cv;
	
	@Before
	public void setup() {
		cv = new CedulaValidator();
	}
	
	@Test
	public void shouldAccept() {
		final ValidatorResponse response = cv.validate("1722452365");
		assertTrue("The cedula should be accepted", response.isValid());
	}
	
	@Test
	public void shouldNotAcceptLengthDifferentThan10() {
		final ValidatorResponse response = cv.validate("01234567890");
		assertFalse("Shouldn't accept 11 digits cedula", response.isValid());
	}
	
	@Test
	public void shouldNotAcceptAlphanumeric() {
		final ValidatorResponse response = cv.validate("172245236A");
		assertFalse("Shouldn't accept alphanumeric string", response.isValid());
	}
	
	@Test
	public void shouldAddMessage() {
		final ValidatorResponse response = cv.validate("012345678AA");
		assertEquals("La cédula debe ser un número de diez dígitos", response.getMessage());
	}
	
}
