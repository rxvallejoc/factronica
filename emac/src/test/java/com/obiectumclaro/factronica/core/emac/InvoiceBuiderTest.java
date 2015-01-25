package com.obiectumclaro.factronica.core.emac;

import com.obiectumclaro.factronica.core.model.xsd.invoice.SignedInvoice;
import com.obiectumclaro.factronica.core.model.xsd.invoice.Tax;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceBuiderTest {

    @Test
    public void shouldGroupDetailsByTaxType() {

        SignedInvoice.Detalles details = new SignedInvoice.Detalles();
        SignedInvoice.Detalles.Detalle detail = getDetalle();
        SignedInvoice.Detalles.Detalle detail1 = getDetalle();
        details.getDetalle().add(detail);
        details.getDetalle().add(detail1);

        List<SignedInvoice.Detalles.Detalle> detalles = details.getDetalle();

        List<Tax> taxes = new ArrayList<>();

        detalles.forEach(d -> {
            taxes.addAll(d.getImpuestos().getImpuesto());

        });

        List<Tax> zeroTaxes = taxes.stream().filter(p -> p.getCodigoPorcentaje().equals("0")).collect(Collectors.toList());
        BigDecimal zeroTaxesValor = zeroTaxes.stream()
                .map(Tax::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal zeroTaxesBaseImponible = zeroTaxes.stream()
                .map(Tax::getBaseImponible)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Tax> twoTaxes = taxes.stream().filter(p -> p.getCodigoPorcentaje().equals("2")).collect(Collectors.toList());
        BigDecimal twoTaxesValor = twoTaxes.stream()
                .map(Tax::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal twoTaxesBaseImponible= twoTaxes.stream()
                .map(Tax::getBaseImponible)
                .reduce(BigDecimal.ZERO, BigDecimal::add);



        System.out.print(taxes);

    }

    private SignedInvoice.Detalles.Detalle getDetalle() {
        SignedInvoice.Detalles.Detalle detail = new SignedInvoice.Detalles.Detalle();
        detail.setCantidad(BigDecimal.ONE);
        SignedInvoice.Detalles.Detalle.Impuestos impuestos = new SignedInvoice.Detalles.Detalle.Impuestos();
        Tax tax = new Tax();
        tax.setCodigo("2");
        tax.setCodigoPorcentaje("0");
        tax.setBaseImponible(BigDecimal.TEN);
        tax.setTarifa(new BigDecimal(12));
        tax.setValor(new BigDecimal(1.2));
        Tax tax1 = new Tax();
        tax1.setCodigo("2");
        tax1.setCodigoPorcentaje("2");
        tax1.setBaseImponible(BigDecimal.TEN);
        tax1.setTarifa(new BigDecimal(12));
        tax1.setValor(new BigDecimal(1.2));
        impuestos.getImpuesto().add(tax);
        impuestos.getImpuesto().add(tax1);
        detail.setImpuestos(impuestos);
        return detail;
    }
}
