package com.obiectumclaro.factronica.api.invoice.domain;

import com.obiectumclaro.factronica.api.invoice.enumeration.Document;
import com.obiectumclaro.factronica.api.invoice.enumeration.Environment;
import com.obiectumclaro.factronica.api.invoice.enumeration.IdentificationType;
import com.obiectumclaro.factronica.api.invoice.enumeration.IssuingMode;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InvoiceBuilder {

    private final Invoice invoice;

    private InvoiceBuilder(final Invoice invoice) {
        this.invoice = invoice;
    }

    public static InvoiceBuilder createInvoice() {
        return new InvoiceBuilder(new Invoice());
    }

    public InvoiceBuilder issuedBy(final String issuerName, final String issuerCommercialName, final String issuerRuc, final String issuerMainAddress, final String issuerCommercialVenue, final String issuerPointOfSale, final String issuerCommercialVenueAddress, final String issuerSpecialTaxPayer) {
        final Issuer issuer = new Issuer(issuerName, issuerCommercialName, issuerRuc, issuerMainAddress, false, issuerCommercialVenue, issuerPointOfSale, issuerCommercialVenueAddress, issuerSpecialTaxPayer);
        invoice.setIssuer(issuer);
        return this;
    }

    public InvoiceBuilder issuedTo(final String customerName, final IdentificationType customerIdType, final String customerId) {
        final Customer customer = new Customer(customerName, customerIdType, customerId);
        invoice.setCustomer(customer);
        return this;
    }

    public InvoiceBuilder issuedToConsumidorFinal() {
        issuedTo("CONSUMIDOR FINAL", IdentificationType.CONSUMIDOR_FINAL, "9999999999999");
        return this;
    }

    public InvoiceBuilder issued(final Date date) {
        invoice.setDateOfIssue(date);
        return this;
    }

    public InvoiceBuilder issuedToday() {
        issued(today());
        return this;
    }

    public Invoice build() {
        if (null == invoice.getIssuer()) {
            throw new IllegalArgumentException("The issuer of the invoice must be specified.");
        }
        if (null == invoice.getCustomer()) {
            issuedToConsumidorFinal();
        }
        if (null == invoice.getDateOfIssue()) {
            issuedToday();
        }
        return invoice;
    }

    private Date today() {
        return Calendar.getInstance().getTime();
    }

    public InvoiceBuilder issuedWithIssueEnviroment(Environment environment) {
        invoice.setEnvironment(environment);
        return this;
    }

    public InvoiceBuilder issuedWithIssuingMode(IssuingMode issuingMode) {
        invoice.setIssuingMode(issuingMode);
        return this;
    }

    public InvoiceBuilder issuedWithDocumentType(Document document) {
        invoice.setDocumentType(document);
        return this;
    }

    public InvoiceBuilder issuedWithAccessKey(String accessKey) {
        invoice.setAccessKey(accessKey);
        return this;
    }

    public InvoiceBuilder issuedWithSequenceNumber(String sequenceNumber) {
        invoice.setSequenceNumber(sequenceNumber);
        return this;
    }

    public InvoiceBuilder issuedTotalWithoutTax(BigDecimal totalWithoutTax) {
        invoice.setTotalWithoutTax(totalWithoutTax);
        return this;
    }

    public InvoiceBuilder issuedTotalDiscount(BigDecimal totalDiscount) {
        invoice.setTotalDiscount(totalDiscount);
        return this;
    }

    public InvoiceBuilder issuedWithTotalAmount(BigDecimal totalAmount) {
        invoice.setTotalAmount(totalAmount);
        return this;
    }

    public InvoiceBuilder issuedWithTip(BigDecimal tip) {
        invoice.setTip(tip);
        return this;
    }

    public InvoiceBuilder issuedWithCurrency(String currency) {
        invoice.setCurrency(currency);
        return this;
    }

    public InvoiceBuilder issuedWithTotalTaxes(List<TotalTax> totalTaxes) {
        invoice.setTotalTaxes(totalTaxes);
        return this;
    }

    public InvoiceBuilder issuedWithDetails(List<Item> details) {
        invoice.setDetails(details);
        return this;
    }
}
