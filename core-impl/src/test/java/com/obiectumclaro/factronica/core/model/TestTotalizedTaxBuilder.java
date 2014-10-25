/**
 *
 */
package com.obiectumclaro.factronica.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.obiectumclaro.factronica.core.enumeration.IvaRateCode;
import com.obiectumclaro.factronica.core.enumeration.TaxCode;

/**
 * Verifications for the just-in-time building of the {@link TotalizedTax}es.
 *
 * @author iapazmino
 *
 */
public class TestTotalizedTaxBuilder {

    private static final BigDecimal HUNDRED = new BigDecimal(100);
    private static final Product MILK = MockProductsFactory.taxedWithIva();
    private static final Product WHISKEY = MockProductsFactory.taxedWithIvaAndIce();
    private TotalizedTaxBuilder builder;
    private List<InvoiceItem> items;

    @Before
    public void initInvoice() {
        builder = new TotalizedTaxBuilder();
        items = new ArrayList<>();
    }

    @Test
    public void shouldHaveAnIvaTax() {
        InvoiceItem itMilk = new InvoiceItem(MILK);
        items.add(itMilk);
//		products.add(MILK);

        final List<TotalizedTax> totalTaxes = builder.getTotalizedTaxes(items);

        assertTotalizedTaxIsFound(TaxCode.IVA, IvaRateCode.TWELVE.code, totalTaxes);
    }

    @Test
    public void shouldHaveBothTaxes() {
        InvoiceItem itWhiskey = new InvoiceItem(WHISKEY);
        items.add(itWhiskey);
//            products.add(WHISKEY);

        final List<TotalizedTax> totalTaxes = builder.getTotalizedTaxes(items);

        assertTotalizedTaxIsFound(TaxCode.IVA, IvaRateCode.TWELVE.code, totalTaxes);
        assertTotalizedTaxIsFound(TaxCode.ICE, MockProductsFactory.ICE_RATE_CODE, totalTaxes);
    }

    @Test
    public void shouldBeTaxedOnProductsPriceWhenIvaAlone() {
        InvoiceItem itMilk = new InvoiceItem(MILK);
        items.add(itMilk);
//        products.add(MILK);

        final List<TotalizedTax> totalTaxes = builder.getTotalizedTaxes(items);
        final TotalizedTax totalIva = find(TaxCode.IVA, IvaRateCode.TWELVE.code, totalTaxes);

        assertEquals(MILK.getUnitPrice(), totalIva.getTaxableBase());
        final BigDecimal taxedValue = MILK.getUnitPrice().multiply(MockProductsFactory.IVA_12.divide(HUNDRED));
        assertEquals(taxedValue, totalIva.getTaxedValue());
    }

    @Test
    public void shouldBeTaxedOnProductsPriceWhenIce() {
//        products.add(WHISKEY);
        InvoiceItem itWhiskey = new InvoiceItem(WHISKEY);
        items.add(itWhiskey);

        final List<TotalizedTax> totalTaxes = builder.getTotalizedTaxes(items);
        final TotalizedTax totalIce = find(TaxCode.ICE, MockProductsFactory.ICE_RATE_CODE, totalTaxes);

        assertEquals(WHISKEY.getUnitPrice(), totalIce.getTaxableBase());
        final BigDecimal taxedValue = WHISKEY.getUnitPrice().multiply(MockProductsFactory.ICE_30.divide(HUNDRED));
        assertEquals(taxedValue, totalIce.getTaxedValue());
    }

    @Test
    public void shouldBeTaxedOnProductsPricePlusIceTaxableWhenIceAndIva() {
//        products.add(WHISKEY);
        InvoiceItem itWhiskey = new InvoiceItem(WHISKEY);
        items.add(itWhiskey);

        final List<TotalizedTax> totalTaxes = builder.getTotalizedTaxes(items);
        final TotalizedTax totalIva = find(TaxCode.IVA, IvaRateCode.TWELVE.code, totalTaxes);

        final BigDecimal taxedWithIce = WHISKEY.getUnitPrice().multiply(MockProductsFactory.ICE_30.divide(HUNDRED));
        final BigDecimal taxableWithIva = WHISKEY.getUnitPrice().add(taxedWithIce);
        assertEquals(taxableWithIva, totalIva.getTaxableBase());
        final BigDecimal taxedWithIva = taxableWithIva.multiply(MockProductsFactory.IVA_12.divide(HUNDRED));
        assertEquals(taxedWithIva, totalIva.getTaxedValue());
    }

    private void assertTotalizedTaxIsFound(final TaxCode taxCode, final String rateCode,
            final List<TotalizedTax> totalTaxes) {
        if (!totalTaxes.contains(new TotalizedTax(taxCode.code, rateCode))) {
            fail("Totalized tax not found: " + taxCode + ", " + rateCode);
        }
    }

    private TotalizedTax find(final TaxCode taxCode, final String rateCode,
            final List<TotalizedTax> totalTaxes) {
        final TotalizedTax toFind = new TotalizedTax(taxCode.code, rateCode);
        final int index = totalTaxes.indexOf(toFind);
        return totalTaxes.get(index);
    }
}
