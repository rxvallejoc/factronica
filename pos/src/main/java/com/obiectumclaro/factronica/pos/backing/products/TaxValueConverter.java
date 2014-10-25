/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.products;

import java.math.BigDecimal;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import com.obiectumclaro.factronica.core.model.Tax;
import com.obiectumclaro.factronica.core.model.TaxValue;

/**
 * Converts tax values from string to object and vice versa.
 * 
 * @author iapazmino
 *
 */
@Named
public class TaxValueConverter implements Converter {

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public TaxValue getAsObject(FacesContext context, UIComponent component, String rawString) {
		final String taxValuePairsString = getKeyValuePairsStringFrom(rawString);
		final String taxPairsString = getKeyValuePairsStringFrom(taxValuePairsString);
		final Tax tax = buildFrom(taxPairsString);
		final TaxValue taxValue = buildTaxValueFrom(taxValuePairsString, tax);
		return taxValue;
	}

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value.toString();
	}
	
	private Tax buildFrom(final String keyValuePairsString) {
		final String[] kvTax = getKeyValuePairsFrom(keyValuePairsString);
		final String taxPK = getValueIn(kvTax[0]);
		final String taxCode = getValueIn(kvTax[1]);
		final Tax tax = new Tax(Long.parseLong(taxPK));
		tax.setCode(Long.parseLong(taxCode));
		return tax;
	}
	
	private TaxValue buildTaxValueFrom(final String keyValuePairsString, final Tax tax) {
		final String[] kvTaxValue = getKeyValuePairsFrom(keyValuePairsString);
		final String pk = getValueIn(kvTaxValue[0]);
		final String rate = getValueIn(kvTaxValue[4]);
		final String taxValueCode = getValueIn(kvTaxValue[1]);
		final TaxValue taxValue = new TaxValue(Long.valueOf(pk));
		taxValue.setRate(new BigDecimal(rate));
		taxValue.setTaxId(tax);
		taxValue.setTaxValueCode(taxValueCode);
		return taxValue;
	}
	
	private String getKeyValuePairsStringFrom(final String rawString) {
		return rawString.substring(rawString.indexOf("[") + 1, rawString.lastIndexOf("]"));
	}
	
	private String[] getKeyValuePairsFrom(final String keyValuePairsString) {
		return keyValuePairsString.split(",");
	}
	
	private String getValueIn(final String keyValuePair) {
		return keyValuePair.substring(keyValuePair.indexOf("=") + 1);
	}

}
