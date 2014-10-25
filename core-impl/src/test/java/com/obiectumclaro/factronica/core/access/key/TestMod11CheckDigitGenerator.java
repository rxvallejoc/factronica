/**
 * 
 */
package com.obiectumclaro.factronica.core.access.key;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

/**
 * @author iapazmino
 *
 */
public class TestMod11CheckDigitGenerator {

	private CheckDigitGenerator generator;
	
	@Before
	public void init() {
		generator = new Mod11CheckDigitGenerator();
	}
	
	@Test
	public void shouldGenerateForUnder6DigitsRoot() {
		final Integer digit = generator.getComputedDigit(new BigDecimal(123));
		assertEquals(Integer.valueOf(6), digit);
	}
	
	@Test
	public void shouldGenerateFor6DigitsRoot() {
		final Integer digit = generator.getComputedDigit(new BigDecimal(111111));
		assertEquals(Integer.valueOf(6), digit);
	}
	
	@Test
	public void shouldGenerateForOver6DigitRoot() {
		final Integer digit = generator.getComputedDigit(new BigDecimal(1111111));
		assertEquals(Integer.valueOf(4), digit);
	}
	
	@Test
	public void shouldGenerateFor50DigitRoot() {
		final BigDecimal root = new BigDecimal("98765432109876543210987654321098765432109876543210");
		final Integer digit = generator.getComputedDigit(root);
		assertEquals(Integer.valueOf(2), digit);
	}
	
	@Test
	public void shouldReturnZeroWhenResultIs11() {
		final Integer digit = generator.getComputedDigit(new BigDecimal(112));
		assertEquals(Integer.valueOf(0), digit);
	}
	
	@Test
	public void shouldReturnOneWhenResultIs10() {
		final Integer digit = generator.getComputedDigit(new BigDecimal(121));
		assertEquals(Integer.valueOf(1), digit);
	}
	
}
