/**
 * 
 */
package com.obiectumclaro.factronica.core.model;

import java.math.BigDecimal;

import com.obiectumclaro.factronica.core.enumeration.IvaRateCode;
import com.obiectumclaro.factronica.core.enumeration.TaxCode;

/**
 * Helper used to create products.
 * 
 * @author iapazmino
 *
 */
public class MockProductsFactory {
	
	public static final BigDecimal IVA_12 = new BigDecimal(12);
	public static final BigDecimal ICE_30 = new BigDecimal(30);
	public static final String ICE_RATE_CODE = "328a";
	
	private static final BigDecimal PRICE_MILK = BigDecimal.ONE;
	private static final BigDecimal PRICE_WHISKEY = BigDecimal.TEN;
	
	public static Product taxedWithIva() {
		final Product product = new Product();
		product.setCode("MLK001");
		product.setName("Milk");
		product.setDescription("Scotland's finest milk");
		product.setUnitPrice(PRICE_MILK);
		
		product.setAmount(BigDecimal.ONE);
		product.setDiscount(BigDecimal.ZERO);
		product.setTotal(PRICE_MILK.multiply(product.getAmount()).subtract(product.getDiscount()));
		
		product.addTaxValue(createIva());
		
		return product;
	}
	
	public static Product taxedWithIvaAndIce() {
		final Product product = new Product();
		product.setCode("WSK001");
		product.setName("Whiskey");
		product.setDescription("Scotland's finest whiskey");
		product.setUnitPrice(PRICE_WHISKEY);
		
		product.setAmount(BigDecimal.ONE);
		product.setDiscount(BigDecimal.ZERO);
		product.setTotal(PRICE_WHISKEY.multiply(product.getAmount()).subtract(product.getDiscount()));
		
		product.addTaxValue(createIva());
		product.addTaxValue(createIce());
		
		return product;
	}
	
	public static TaxValue createIva() {
		final Tax ivaType = new Tax();
		ivaType.setDescription("IVA 12%");
		ivaType.setCode(TaxCode.IVA.code);
		
		final TaxValue iva = new TaxValue();
		iva.setPk(1L);
		iva.setTaxIdpk(ivaType);
		iva.setTaxable(PRICE_MILK);
		iva.setRate(IVA_12);
		iva.setTaxValueCode(IvaRateCode.TWELVE.code);
		
		return iva;
	}
	
	private static TaxValue createIce() {
		final Tax iceType = new Tax();
		iceType.setDescription("ICE 30%");
		iceType.setCode(TaxCode.ICE.code);
		
		final TaxValue ice = new TaxValue();
		ice.setPk(2L);
		ice.setTaxIdpk(iceType);
		ice.setTaxable(PRICE_WHISKEY);
		ice.setRate(ICE_30);
		ice.setTaxValueCode(ICE_RATE_CODE);
		
		return ice;
	}

}
