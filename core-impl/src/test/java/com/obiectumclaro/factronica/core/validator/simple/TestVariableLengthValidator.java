/**
 * 
 */
package com.obiectumclaro.factronica.core.validator.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.obiectumclaro.factronica.core.validator.Interval;
import com.obiectumclaro.factronica.core.validator.ValidatorRequest;
import com.obiectumclaro.factronica.core.validator.ValidatorResponse;
import com.obiectumclaro.factronica.core.validator.simple.SimpleValidator;
import com.obiectumclaro.factronica.core.validator.simple.VariableLengthValidator;

/**
 * Test cases to verify the {@link VariableLengthValidator}.
 * 
 * @author iapazmino
 *
 */
public class TestVariableLengthValidator {

	private SimpleValidator<Interval> rv;
	private ValidatorRequest<Interval> request;
	private Interval expected;
	
	@Before
	public void setup() {
		rv = new VariableLengthValidator();
		request = new ValidatorRequest<Interval>();
		expected = new Interval(2, 4);
		request.setExpected(expected);
	}
	
	@Test
	public void shouldAccept() {
		request.setActual("123");
		final ValidatorResponse resp = rv.validate(request);
		assertTrue("The string length is between the range", resp.isValid());
	}
	
	@Test
	public void shouldNotAcceptIfOverTheMax() {
		request.setActual("12345");
		final ValidatorResponse resp = rv.validate(request);
		assertFalse("The string length is over the range", resp.isValid());
	}
	
	@Test
	public void shouldNotAcceptIfUnderMin() {
		request.setActual("1");
		final ValidatorResponse resp = rv.validate(request);
		assertFalse("The string length is under the range", resp.isValid());
	}
	
	@Test
	public void shouldAcceptIncludingMaxEndpoint() {
		request.setActual("1234");
		final ValidatorResponse resp = rv.validate(request);
		assertTrue("The string length is between the range", resp.isValid());
	}
	
	@Test
	public void shouldAcceptIncludingMinEndpoint() {
		request.setActual("12");
		final ValidatorResponse resp = rv.validate(request);
		assertTrue("The string length is between the range", resp.isValid());
	}
	
	@Test
	public void shouldNotAcceptByExcludingMaxEndpoint() {
		expected.setInclusive(false);
		request.setExpected(expected);
		request.setActual("1234");
		final ValidatorResponse resp = rv.validate(request);
		assertFalse("The string length is over the range", resp.isValid());
	}
	
	@Test
	public void shouldNotAcceptByExcludingMinEndpoint() {
		expected.setInclusive(false);
		request.setExpected(expected);
		request.setActual("12");
		final ValidatorResponse resp = rv.validate(request);
		assertFalse("The string length is over the range", resp.isValid());
	}
	
	@Test
	public void shouldAddInclusiveMessage() {
		request.setActual("12345");
		final ValidatorResponse resp = rv.validate(request);
		assertEquals("Se esperaba una longitud entre <2> y <4> inclusiva pero en su lugar se obtuvo <5>", 
				resp.getMessage());
	}
	
	@Test
	public void shouldAddExclusiveMessage() {
		expected.setInclusive(false);
		request.setExpected(expected);
		request.setActual("12345");
		final ValidatorResponse resp = rv.validate(request);
		assertEquals("Se esperaba una longitud entre <2> y <4> exclusiva pero en su lugar se obtuvo <5>", 
				resp.getMessage());
	}
	
}
