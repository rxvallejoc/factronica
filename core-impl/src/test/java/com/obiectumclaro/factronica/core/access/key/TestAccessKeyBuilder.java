/**
 * 
 */
package com.obiectumclaro.factronica.core.access.key;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.obiectumclaro.factronica.core.enumeration.DocumentType;
import com.obiectumclaro.factronica.core.enumeration.Environment;
import com.obiectumclaro.factronica.core.enumeration.IssuingMode;

/**
 * @author iapazmino
 *
 */
public class TestAccessKeyBuilder {
	
	private static final String RUC = "1762321202001";
	private static final String SERIAL = "001001";
	private static final String SEQUENCE = "000000001";

	private AccessKeyBuilder builder;
	private CheckDigitGenerator checkGenerator;
	
	@Before
	public void init() {
		builder = new AccessKeyBuilder()
			.ruc(RUC)
			.serial(SERIAL)
			.sequence(SEQUENCE);
		
		checkGenerator = mock(CheckDigitGenerator.class);
		builder.setCheckDigitGenerator(checkGenerator);
		when(checkGenerator.getComputedDigit(any(BigDecimal.class))).thenReturn(0);
	}
	
	@Test
	public void shouldBe49CharsLong() {
		final String key = builder.createAccessKey().getFullKey();
		
		final int length = key.length();
		assertTrue("Acess key's length was " + length, 49 == length);
	}
	
	@Test
	public void shouldFindDateBetween0And8() throws ParseException {
		final String strDate = "30/01/2013";
		final Date date = new SimpleDateFormat("dd/MM/yyyy").parse(strDate);
		builder.issued(date);
		
		final String key = builder.createAccessKey().getFullKey();
		
		assertEquals("30012013", key.subSequence(0, 8));
	}
	
	@Test
	public void shouldDefaultDateToToday() {
		
		final String key = builder.createAccessKey().getFullKey();
		
		final String strDate = AccessKeyBuilder.FORMATTER_DATE.format(Calendar.getInstance().getTime());
		assertEquals(strDate, key.subSequence(0, 8));
	}
	
	@Test
	public void souldFindDocTypeBetween8And10() {
		builder.type(DocumentType.FACTURA);
		
		final String key = builder.createAccessKey().getFullKey();
		
		assertEquals(DocumentType.FACTURA.code, key.subSequence(8, 10));
	}
	
	@Test
	public void shouldDefaultTypeToFactura() {
		
		final String key = builder.createAccessKey().getFullKey();
		
		assertEquals(DocumentType.FACTURA.code, key.subSequence(8, 10));
	}
	
	@Test
	public void souldFindRUCBetween10And23() {
		
		final String key = builder.createAccessKey().getFullKey();
		
		assertEquals(RUC, key.subSequence(10, 23));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldAcceptOnly13DigitLongRuc() {
		builder.ruc("");
		
		builder.createAccessKey().getFullKey();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldFailIfNoRucWasProvided() {
		builder = new AccessKeyBuilder();
		
		builder.createAccessKey().getFullKey();
	}
	
	@Test
	public void shouldFinEnvironmentBetween23And24() {
		builder.environment(Environment.PRODUCCION);
		
		final String key = builder.createAccessKey().getFullKey();
		
		final String env = key.subSequence(23, 24).toString();
		assertEquals(Environment.PRODUCCION.ordinal(), Integer.parseInt(env));
	}
	
	@Test
	public void shouldDefaultEnvironmentToProduccion() {
		final String key = builder.createAccessKey().getFullKey();
		
		final String env = key.subSequence(23, 24).toString();
		assertEquals(Environment.PRODUCCION.ordinal(), Integer.parseInt(env));
	}
	
	@Test
	public void shouldFindSerialBetween24And30() {
		
		final String key = builder.createAccessKey().getFullKey();
		
		assertEquals(SERIAL, key.subSequence(24, 30));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldAcceptOnly6DigitLongSerial() {
		builder.serial("");
		
		builder.createAccessKey().getFullKey();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldFailIfNoSerialWasProvided() {
		builder = new AccessKeyBuilder()
			.ruc(RUC);
		
		builder.createAccessKey().getFullKey();
	}
	
	@Test
	public void shouldFindSequenceBetween30And39() {
		
		final String key = builder.createAccessKey().getFullKey();
		
		assertEquals(SEQUENCE, key.subSequence(30, 39));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldAcceptOnly9DigitLongSequence() {
		builder.sequence("");
		
		builder.createAccessKey().getFullKey();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldFailIfNoSequenceWasProvided() {
		builder = new AccessKeyBuilder()
			.ruc(RUC)
			.serial(SERIAL);
		
		builder.createAccessKey().getFullKey();
	}
	
	@Test
	public void shouldFindGeneratedCodeBetween39And47() {
		builder.generatedCode(8L);
		
		final String key = builder.createAccessKey().getFullKey();
		
		assertEquals("00000008", key.subSequence(39, 47));
	}
	
	@Test
	public void shouldProvideDefaultGeneratedCode() {
		final String key = builder.createAccessKey().getFullKey();
		
		final String genCode = key.subSequence(39, 47).toString();
		assertTrue("Generated code was " + genCode, Long.valueOf(genCode) > 0);
	}
	
	@Test
	@Ignore("To be taken into account when masive generation is set in place")
	public void shouldProvideDifferentDefaults() {
		final Set<String> generatedCodes = new HashSet<>();
		AccessKey key = null;
		String generatedCode = null;
		
		for (int i = 0; i < 100; i++) {
			key = builder.createAccessKey();
			generatedCode = key.getGeneratedCode();
			if (!generatedCodes.add(generatedCode)) {
				fail("Repeated " + generatedCode + " in loop " + i);
			}
		}
	}
	
	@Test
	public void shouldFindIssuingModeBetween47And48() {
		builder.issuingMode(IssuingMode.NORMAL);
		
		final String key = builder.createAccessKey().getFullKey();
		
		final String mode = key.subSequence(47, 48).toString();
		assertEquals(IssuingMode.NORMAL.ordinal()+1, Integer.parseInt(mode));
	}
	
	@Test
	public void shouldDefaultIssuingModeToNormal() {		
		final String key = builder.createAccessKey().getFullKey();
		
		final String mode = key.subSequence(47, 48).toString();
		assertEquals(IssuingMode.NORMAL.ordinal()+1, Integer.parseInt(mode));
	}
	
	@Test
	public void shouldFindCheckDigitBetween48And49() {
		final String key = builder.createAccessKey().getFullKey();
		
		final String digit = key.subSequence(48, 49).toString();
		assertTrue("Check digit was " + digit, Integer.valueOf(digit).equals(0));
	}
	
}
