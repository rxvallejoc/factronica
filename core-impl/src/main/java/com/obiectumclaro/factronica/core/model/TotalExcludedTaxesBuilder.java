/**
 *
 */
package com.obiectumclaro.factronica.core.model;

import com.obiectumclaro.factronica.core.model.access.ProductEaoBean;
import com.obiectumclaro.factronica.core.model.xsd.nota.credito.NotaCredito;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;

/**
 * Calculates the total excluding taxes.
 *
 * @author iapazmino
 *
 */
public class TotalExcludedTaxesBuilder {

    @EJB
    ProductEaoBean productBean;

    public BigDecimal getTotalExcludedTaxes(final List<InvoiceItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (InvoiceItem product : items) {
            total = total.add(product.getTotal());
        }
        return total;
    }

    public BigDecimal getTotalExcludedTaxesNC(final List<NotaCredito.Detalles.Detalle> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (NotaCredito.Detalles.Detalle product : items) {
            total = total.add(product.getPrecioTotalSinImpuesto());
        }
        return total;
    }
}
