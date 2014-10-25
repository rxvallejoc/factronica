/**
 * 
 */
package com.obiectumclaro.factronica.core.model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test calculation for total excluded taxes.
 * 
 * @author iapazmino
 *
 */
public class TestTotalExcludedTaxesBuilder {
	
	private static final Product MILK = MockProductsFactory.taxedWithIva();
	private static final Product WHISKEY = MockProductsFactory.taxedWithIvaAndIce();

	private TotalExcludedTaxesBuilder builder;
	private List<InvoiceItem> items;
	
	@Before
	public void init() {
		builder = new TotalExcludedTaxesBuilder();
		items = new ArrayList<>();
	}
	
	@Before
	public void restart() {
		MILK.setAmount(BigDecimal.ONE);
		WHISKEY.setAmount(BigDecimal.ONE);
	}
	
	@Test
	public void shouldEqualsToProductsPrice() {
                InvoiceItem itMilk= new InvoiceItem(MILK);
		items.add(itMilk);
//		products.add(MILK);
		
		final BigDecimal total = builder.getTotalExcludedTaxes(items);
		
		assertEquals(MILK.getUnitPrice(), total);
	}
	
	@Test
	public void shouldAddBothPrices() {
                InvoiceItem itMilk= new InvoiceItem(MILK);
		items.add(itMilk);
//		products.add(MILK);
                InvoiceItem itWhiskey= new InvoiceItem(WHISKEY);
		items.add(itWhiskey);
//		products.add(WHISKEY);
		
		final BigDecimal total = builder.getTotalExcludedTaxes(items);
		
		assertEquals(MILK.getUnitPrice().add(WHISKEY.getUnitPrice()), total);
	}
	
	@Test
	public void shouldAddBothTotalPrices() {
		InvoiceItem itMilk= new InvoiceItem(MILK);
		items.add(itMilk);
//		products.add(MILK);
                InvoiceItem itWhiskey= new InvoiceItem(WHISKEY);
		items.add(itWhiskey);
		
		final BigDecimal total = builder.getTotalExcludedTaxes(items);
		
		final BigDecimal totalForMilk = MILK.getTotal().add(WHISKEY.getTotal());
		assertEquals(totalForMilk, total);
	}
	
}
