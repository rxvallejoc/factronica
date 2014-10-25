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
import com.obiectumclaro.factronica.core.validator.compound.RUCValidator;

/**
 * @author iapazmino
 *
 */
public class TestRUCValidator {

	private CompoundValidator rv;
	
	@Before
	public void setup() {
		rv = new RUCValidator();
	}
	
	@Test
	public void shouldAccept() {
		final ValidatorResponse response = rv.validate("1176752345001");
		assertTrue("Should accept valid RUC", response.isValid());
	}
	
	@Test
	public void shouldNotAcceptRUCWithLengthOtherThan10() {
		final ValidatorResponse response = rv.validate("11767523450012");
		assertFalse("Shouldn't accept RUC with length of twelve", response.isValid());
	}
	
	@Test
	public void shouldNotAcceptAlphanumericRUC() {
		final ValidatorResponse response = rv.validate("1176752345ABC");
		assertFalse("Shouldn't accept alphanumeric RUC", response.isValid());
	}
	
	@Test
	public void shouldAddMessage() {
		final ValidatorResponse response = rv.validate("1176752345ABC");
		assertEquals("El RUC debe ser un número de 13 dígitos", response.getMessage());
	}
	
}
