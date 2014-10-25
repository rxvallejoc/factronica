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
 * Test the calculation of total discount.
 * 
 * @author iapazmino
 *
 */
public class TestTotalDiscountBuilder {
	
	private static final Product MILK = MockProductsFactory.taxedWithIva();
	private static final Product WHISKEY = MockProductsFactory.taxedWithIvaAndIce();
	
	private static final BigDecimal DISCOUNT = new BigDecimal(0.5);
	private static final BigDecimal QUANTITY = new BigDecimal(2);
	
	private TotalDiscountBuilder builder;
	private List<InvoiceItem> items;
	
	@Before
	public void init() {
		builder = new TotalDiscountBuilder();
		items = new ArrayList<>();
		
		MILK.setDiscount(DISCOUNT);
		WHISKEY.setDiscount(DISCOUNT);
	}
	
	@Before
	public void restart() {
		MILK.setAmount(BigDecimal.ONE);
		WHISKEY.setAmount(BigDecimal.ONE);
	}
	
	@Test
	public void shouldEqualsToProductsDiscount() {
		items.add(new InvoiceItem(MILK));
		
		final BigDecimal total = builder.getTotalDiscount(items);
		
		assertEquals(MILK.getDiscount(), total);
	}
	
	@Test
	public void shouldNotTakeIntoAccountQuantity() {
		MILK.setAmount(QUANTITY);
		items.add(new InvoiceItem(MILK));
		
		final BigDecimal total = builder.getTotalDiscount(items);
		
		final BigDecimal discount = MILK.getDiscount();
		assertEquals(discount, total);
	}
	
	@Test
	public void shouldAddBothPrices() {
		items.add(new InvoiceItem(MILK));
		items.add(new InvoiceItem(WHISKEY));
		
		final BigDecimal total = builder.getTotalDiscount(items);
		
		assertEquals(MILK.getDiscount().add(WHISKEY.getDiscount()), total);
	}
	
	@Test
	public void shouldCountDiscountPerProduct() {
		MILK.setAmount(QUANTITY);
		WHISKEY.setAmount(QUANTITY);
		items.add(new InvoiceItem(MILK));
		items.add(new InvoiceItem(WHISKEY));
		
		final BigDecimal total = builder.getTotalDiscount(items);
		
		final BigDecimal totalDiscount = MILK.getDiscount().add(WHISKEY.getDiscount());
		assertEquals(totalDiscount, total);
	}

}
