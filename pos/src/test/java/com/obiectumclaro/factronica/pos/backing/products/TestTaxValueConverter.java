/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.products;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.obiectumclaro.factronica.core.model.Tax;
import com.obiectumclaro.factronica.core.model.TaxValue;

/**
 * Checks the result string from {@link TaxValue#toString()}, for example
 * {@link #TO_STRING}, is properly converted into a {@link TaxValue} object.
 * 
 * @author iapazmino
 * 
 */
public class TestTaxValueConverter {

	private static final String TO_STRING = "TaxValue [pk=4, taxValueCode=2, description=IVA 12, taxable=0.00, rate=12.00, satartDate=2012-12-02, endDate=2013-07-31, productList=, taxId=Tax [pk=2, code=2, description=IVA]]";
	
	private TaxValueConverter converter;
	private TaxValue result;

	@Before
	public void init() {
		converter = new TaxValueConverter();
		result = converter.getAsObject(null, null, TO_STRING);
	}

	@Test
	public void shouldReadPk() {
		assertEquals(Long.valueOf(4), result.getPk());
	}
	
	@Test
	public void shouldReadRate() {
		assertEquals(new BigDecimal("12.00"), result.getRate());
	}
	
	@Test
	public void shouldReadTax() {
		final Tax tax = new Tax(2L);
		tax.setCode(2L);
		tax.setDescription("IVA");
		tax.setStateTax("ACT");
		assertEquals(tax, result.getTaxId());
	}
	
	@Test
	public void shouldReadTaxValueCode() {
		assertEquals("2", result.getTaxValueCode());
	}
}
