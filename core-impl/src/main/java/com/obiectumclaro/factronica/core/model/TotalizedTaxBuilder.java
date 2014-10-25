/**
 *
 */
package com.obiectumclaro.factronica.core.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.obiectumclaro.factronica.core.model.xsd.nota.credito.NotaCredito;

/**
 * Class in charge of building {@link TotalizedTax} instances for a given
 * {@link Product}s list.
 *
 * @author iapazmino
 *
 */
public class TotalizedTaxBuilder {

    private static final BigDecimal HUNDRED = new BigDecimal(100);
    
    private final Set<TotalizedTax> totals;
    private final Map<TotalizedTax, BigDecimal> taxables;
    private final Map<TotalizedTax, BigDecimal> taxedValues;

    public TotalizedTaxBuilder() {
        totals = new HashSet<TotalizedTax>();
        taxables = new HashMap<TotalizedTax, BigDecimal>();
        taxedValues = new HashMap<TotalizedTax, BigDecimal>();
    }

    @SuppressWarnings("unused")
	public List<TotalizedTax> getTotalizedTaxes(List<InvoiceItem> items) {
        List<TaxValue> taxes = null;
        Tax tax = null;
        TotalizedTax total = null;
        for (InvoiceItem item : items) {
            taxes = item.getProduct().getTaxValueList();
            TaxValue ice = lookForIceTaxIn(taxes);
            for (TaxValue currentTax : taxes) {
                BigDecimal taxableBase = calculateTaxableBase(item.getProduct(), currentTax, ice);
                BigDecimal taxedValue = calculateTaxedValue(taxableBase, currentTax);
                tax = currentTax.getTaxId();
                total = new TotalizedTax(currentTax.getTaxId().getCode(), currentTax.getTaxValueCode());
                total.setTaxDescription(currentTax.getTaxId().getDescription());
                total.setRate(currentTax.getRate().longValue());
                boolean isANewTax = totals.add(total);
                saveTaxableBase(total, taxableBase, isANewTax);
                saveTaxedValue(total, taxedValue, isANewTax);
            }
        }

        return arrangedAsOneList();
    }

    private TaxValue lookForIceTaxIn(final List<TaxValue> taxes) {
        TaxValue ice = null;
        for (TaxValue taxValue : taxes) {
            if (taxValue.isICE()) {
                ice = taxValue;
            }
        }
        return ice;
    }

    private BigDecimal calculateTaxableBase(final Product product, final TaxValue currentTax,
            final TaxValue ice) {
        boolean productIsTaxedWithIce = (null != ice);
        BigDecimal iceTaxedValue = null;
        if (productIsTaxedWithIce && currentTax.isIVA()) {
            iceTaxedValue = product.getUnitPrice().multiply(ice.getRate().divide(HUNDRED)).multiply(product.getAmount());
            return product.getUnitPrice().add(iceTaxedValue);
        } else {
            return product.getUnitPrice().multiply(product.getAmount()).subtract(product.getDiscount());
        }
    }

    private BigDecimal calculateTaxableBaseNC(final NotaCredito.Detalles.Detalle detalle , final TaxValue currentTax,
            final TaxValue ice) {
        boolean productIsTaxedWithIce = (null != ice);
        BigDecimal iceTaxedValue = null;
        if (productIsTaxedWithIce && currentTax.isIVA()) {
            iceTaxedValue = detalle.getPrecioUnitario().multiply(ice.getRate().divide(HUNDRED)).multiply(detalle.getCantidad());
            return detalle.getPrecioUnitario().add(iceTaxedValue);
        } else {
            return detalle.getPrecioUnitario().multiply(detalle.getCantidad());
        }
    }
    
    private BigDecimal calculateTaxedValue(final BigDecimal taxableBase, final TaxValue currentTax) {
        return taxableBase.multiply(currentTax.getRate().divide(HUNDRED));
    }

    private void saveTaxableBase(final TotalizedTax total, final BigDecimal taxableBase,
            final boolean isANewTax) {
        if (isANewTax) {
            taxables.put(total, taxableBase);
        } else {
            BigDecimal currentTaxable = taxables.get(total);
            taxables.put(total, currentTaxable.add(taxableBase));
        }
    }

    private void saveTaxedValue(final TotalizedTax total, final BigDecimal taxedValue,
            final boolean isANewTax) {
        if (isANewTax) {
            taxedValues.put(total, taxedValue);
        } else {
            BigDecimal currentTaxedValue = taxedValues.get(total);
            taxedValues.put(total, currentTaxedValue.add(taxedValue));
        }
    }

    private List<TotalizedTax> arrangedAsOneList() {
        final List<TotalizedTax> result = new ArrayList<TotalizedTax>();
        for (TotalizedTax totalizedTax : totals) {
            BigDecimal totalTaxable = taxables.get(totalizedTax);
            BigDecimal totalTaxedValue = taxedValues.get(totalizedTax);
            totalizedTax.setTaxableBase(totalTaxable);
            totalizedTax.setTaxedValue(totalTaxedValue);
            result.add(totalizedTax);
        }

        return result;
    }

    @SuppressWarnings("unused")
	public List<TotalizedTax> getTotalizedTaxesNC(List<NotaCredito.Detalles.Detalle> items) {
        List<TaxValue> taxes = null;
        Tax tax = null;
        TotalizedTax total = null;

        for (NotaCredito.Detalles.Detalle item : items) {
            if (item!= null && item.getCodigoInterno()!=null) {
//                System.out.println("------->"+item.getCodigoInterno()+ "   "+productBean);
//                Product p = productBean.findByCode(item.getCodigoInterno());
//                System.out.println("p------>"+p);
                if (item.getProduct() != null) {
                    taxes = item.getProduct().getTaxValueList();
                    TaxValue ice = lookForIceTaxIn(taxes);
                    for (TaxValue currentTax : taxes) {
                        BigDecimal taxableBase = calculateTaxableBaseNC(item, currentTax, ice);
                        BigDecimal taxedValue = calculateTaxedValue(taxableBase, currentTax);
                        tax = currentTax.getTaxId();
//				total = new TotalizedTax(tax.getCode(), tax.getRateCode());
                        total = new TotalizedTax(currentTax.getTaxId().getCode(), currentTax.getTaxValueCode());
                        total.setTaxDescription(currentTax.getTaxId().getDescription());
                        total.setRate(currentTax.getRate().longValue());
                        boolean isANewTax = totals.add(total);
                        saveTaxableBase(total, taxableBase, isANewTax);
                        saveTaxedValue(total, taxedValue, isANewTax);
                    }
                }
            }
        }
        return arrangedAsOneList();
    }
}
