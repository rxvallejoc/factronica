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
import com.obiectumclaro.factronica.core.validator.compound.CompoundValidator;
import com.obiectumclaro.factronica.core.validator.compound.PasaporteValidator;

/**
 * Test cases to verify {@link PasaporteValidator}.
 * 
 * @author iapazmino
 *
 */
public class TestPasaporteValidator {

	private CompoundValidator pv;
	
	@Before
	public void setup() {
		pv = new PasaporteValidator();
	}
	
	@Test
	public void shouldAccept() {
		final ValidatorResponse response = pv.validate("CD-653");
		assertTrue("Should accept pasaporte", response.isValid());
	}
	
	@Test
	public void shouldNotAcceptLengthUnder3() {
		final ValidatorResponse response = pv.validate("CD");
		assertFalse("Shouldn't accept pasaporte with length of two", response.isValid());
	}
	
	@Test
	public void shouldNotAcceptLengthOver10() {
		final ValidatorResponse response = pv.validate("ABCD-123456");
		assertFalse("Shouldn't accept pasaporte with length of eleven", response.isValid());
	}
	
	@Test
	public void shouldAddMessage() {
		final ValidatorResponse response = pv.validate("CD");
		assertEquals("El pasaporte es una cadena alfanumérica de mínimo 3 y máximo 10 caracteres", 
				response.getMessage());
	}
	
}
