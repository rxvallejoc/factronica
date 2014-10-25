/**
 * 
 */
package com.obiectumclaro.factronica.core.model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

/**
 * @author iapazmino
 *
 */
public class TestTotalDueBuilder {
	
	private static final Product MILK = MockProductsFactory.taxedWithIva();
	private static final TaxValue IVA = MILK.getTaxValueList().get(0);
	private static final BigDecimal TAXES_FOR_MILK = MILK.getUnitPrice().multiply(IVA.getRate().divide(new BigDecimal(100)));
	
	private static final BigDecimal TIP = BigDecimal.ONE;

	private TotalDueBuilder builder;
	private Invoice invoice;
	
	@Before
	public void init() {
		builder = new TotalDueBuilder();
		invoice = new Invoice();
	}
	
	@Test
	public void shouldEqualsToPricePlusTaxes() {
                InvoiceItem itMilk= new InvoiceItem(MILK);
		invoice.getItems().add(itMilk);
		
		final BigDecimal totalDue = builder.getTotalDue(invoice);
		
		final BigDecimal expected = MILK.getUnitPrice().add(TAXES_FOR_MILK);
		assertEquals(expected, totalDue);
	}
	
	@Test
	public void shouldEqualsToPricePlusTaxesAndTip() {
                InvoiceItem itMilk= new InvoiceItem(MILK);
		invoice.getItems().add(itMilk);
//		invoice.getItems(MILK);
		invoice.setTip(TIP);
		
		final BigDecimal totalDue = builder.getTotalDue(invoice);
		
		final BigDecimal expected = MILK.getUnitPrice().add(TAXES_FOR_MILK).add(TIP);
		assertEquals(expected, totalDue);
	}
	
}
