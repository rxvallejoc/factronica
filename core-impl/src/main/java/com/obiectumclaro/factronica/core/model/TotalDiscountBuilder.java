/**
 * 
 */
package com.obiectumclaro.factronica.core.model;

import com.obiectumclaro.factronica.core.model.xsd.nota.credito.NotaCredito;

import java.math.BigDecimal;
import java.util.List;

/**
 * Sums the discounts in all products.
 * 
 * @author iapazmino
 *
 */
public class TotalDiscountBuilder {

	public BigDecimal getTotalDiscount(final List<InvoiceItem> items) {
		BigDecimal total = BigDecimal.ZERO;
		for (InvoiceItem item : items) {
			total = total.add(item.getDiscount());
		}
		return total;
	}
        
        public BigDecimal getTotalDiscountNC(List<NotaCredito.Detalles.Detalle> items) {
		BigDecimal total = BigDecimal.ZERO;
		for (NotaCredito.Detalles.Detalle item : items) {
			if(item.getDescuento()!=null){
                            total = total.add(item.getDescuento());
                        }else{
                            total = total.add(BigDecimal.ZERO);
                        }
		}
		return total;
	}

}
