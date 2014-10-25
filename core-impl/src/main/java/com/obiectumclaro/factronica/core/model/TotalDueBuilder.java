/**
 * 
 */
package com.obiectumclaro.factronica.core.model;


import com.obiectumclaro.factronica.core.model.xsd.nota.credito.NotaCredito;

import java.math.BigDecimal;

/**
 * Calculates the total due for the invoice.
 * 
 * @author iapazmino
 *
 */
public class TotalDueBuilder {

	public BigDecimal getTotalDue(final Invoice invoice) {
		BigDecimal totalTaxes = BigDecimal.ZERO;
		for (TotalizedTax totalizedTax : invoice.getTotalizedTaxes()) {
			totalTaxes = totalTaxes.add(totalizedTax.getTaxedValue());
		}
		return invoice.getTotalExcludedTaxes().add(totalTaxes).add(invoice.getTip()).subtract(invoice.getTotalDiscount());
	}
        
        public BigDecimal getTotalDueNC(final NotaCredito invoice) {
		BigDecimal totalTaxes = BigDecimal.ZERO;
                System.out.println("----->"+invoice.getTotalizedTaxes());
		for (TotalizedTax totalizedTax : invoice.getTotalizedTaxes()) {
			totalTaxes = totalTaxes.add(totalizedTax.getTaxedValue());
		}
//		return invoice.getTotalExcludedTaxes().add(totalTaxes).add(invoice.get).subtract(invoice.getTotalDiscount());
                
                if(invoice.getTotalDiscount()!=null){
                    return invoice.getTotalExcludedTaxes().add(totalTaxes).subtract(invoice.getTotalDiscount());
                }
                return invoice.getTotalExcludedTaxes().add(totalTaxes);
	}
}
