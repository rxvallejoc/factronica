package com.obiectumclaro.factronica.api.invoice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.obiectumclaro.factronica.api.invoice.domain.*;
import com.obiectumclaro.factronica.api.invoice.enumeration.Document;
import com.obiectumclaro.factronica.api.invoice.enumeration.Environment;
import com.obiectumclaro.factronica.api.invoice.enumeration.IdentificationType;
import com.obiectumclaro.factronica.api.invoice.enumeration.IssuingMode;
import com.obiectumclaro.factronica.api.invoice.domain.InvoiceBuilder;
import org.junit.Test;

public class TestInvoiceBuilder {

    @Test(expected = IllegalArgumentException.class)
    public void shouldExpectAnIssuer() {
        InvoiceBuilder.createInvoice().build();
    }

    @Test
    public void shouldSpecifyIssuer() {
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .build();

        assertAreEquals("Seller Inc.", "Seller Venue","1710335267001", "Av. Principal", false,"001", "501", "Av. Amazonas", "12345", invoice.getIssuer());
    }

    @Test
    public void shouldIssueToConsumidorFinal() {
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issuedToConsumidorFinal()
                .build();

        assertAreEquals("CONSUMIDOR FINAL", IdentificationType.CONSUMIDOR_FINAL, "9999999999999", invoice.getCustomer());
    }

    @Test
    public void shouldIssueToSpecificCustomer() {
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issuedTo("OC Inc.", IdentificationType.RUC, "1712883480001")
                .build();

        assertAreEquals("OC Inc.", IdentificationType.RUC, "1712883480001", invoice.getCustomer());
    }

    @Test
    public void shouldIssueToConsumidorFinalByDefault() {
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .build();

        assertAreEquals("CONSUMIDOR FINAL", IdentificationType.CONSUMIDOR_FINAL, "9999999999999", invoice.getCustomer());
    }

    @Test
    public void shouldBeIssuedOnSpecifiedDate() {
        final Date yesterday = yesterday();
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issued(yesterday)
                .build();

        assertEquals(yesterday, invoice.getDateOfIssue());
    }

    @Test
    public void shouldDefaultTodayAsDateOfIssue() {
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .build();

        assertNotNull(invoice.getDateOfIssue());
    }

    @Test
    public void shouldBeIssuedWithProductionEnvironment() {
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issuedWithIssueEnviroment(Environment.PRODUCTION)
                .build();

        assertEquals(Environment.PRODUCTION,invoice.getEnvironment());
    }

    @Test
    public void shouldBeIssuedWithTestEnvironment(){
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issuedWithIssueEnviroment(Environment.TEST)
                .build();

        assertEquals(Environment.TEST,invoice.getEnvironment());

    }

    @Test
    public void shouldBeIssuedWithIssuingModeNormal(){
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issuedWithIssuingMode(IssuingMode.NORMAL)
                .build();

        assertEquals(IssuingMode.NORMAL,invoice.getIssuingMode());

    }

    @Test
    public void shouldBeIssuedWithIssuingModeContingency(){
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issuedWithIssuingMode(IssuingMode.CONTINGENCY)
                .build();

        assertEquals(IssuingMode.CONTINGENCY,invoice.getIssuingMode());
    }

    @Test
    public void  shouldBeIssuedWithInvoiceDocumentType (){
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issuedWithDocumentType(Document.FACTURA)
                .build();

        assertEquals(Document.FACTURA,invoice.getDocumentType());
    }

    @Test
    public void shouldBeIssuedWithAccessKey(){
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issuedWithAccessKey("2103201301000000000000110015010000000101234567811")
                .build();

        assertEquals("2103201301000000000000110015010000000101234567811",invoice.getAccessKey());

    }

    @Test
    public void shouldBeIssuedWithSequenceNumber(){
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issuedWithSequenceNumber("000000010")
                .build();

        assertEquals("000000010",invoice.getSequenceNumber());
    }

    @Test
    public void shouldBeIssuedWithTotalWithoutTax(){
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issuedTotalWithoutTax(new BigDecimal(0.00))
                .build();

        assertEquals(new BigDecimal(0.00),invoice.getTotalWithoutTax());
    }

    @Test
    public void shouldBeIssuedWithTotalDiscount(){
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issuedTotalDiscount(new BigDecimal(0.00))
                .build();

        assertEquals(new BigDecimal(0.00),invoice.getTotalDiscount());

    }

    @Test
    public void shouldBeIssuedWithTotalAmount(){
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issuedWithTotalAmount(new BigDecimal(1.00))
                .build();


        assertEquals(new BigDecimal(1.00),invoice.getTotalAmount());

    }

    @Test
    public void shouldBeIssuedWithTip(){
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issuedWithTip(new BigDecimal(1.00))
                .build();


        assertEquals(new BigDecimal(1.00),invoice.getTip());
    }

    @Test
    public void shouldBeIssuedWithCurrency(){
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issuedWithCurrency("USD")
                .build();


        assertEquals("USD",invoice.getCurrency());

    }

    @Test
    public void shouldBeIssuedWithTotalTaxes(){
        List<TotalTax> totalTaxes = new ArrayList<TotalTax>() {{
            add(new TotalTax("2", "6", new BigDecimal(0.00), new BigDecimal(0.00)));
        }};

        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issuedWithTotalTaxes(totalTaxes)
                .build();


        assertEquals(totalTaxes,invoice.getTotalTaxes());

    }

    @Test
    public void shouldBeIssuedWithDetails(){
        List<Item> details = new ArrayList<Item>(){{add(new Item("COCA01","Coca Cola",1,new BigDecimal(1.25),new BigDecimal(0.20),new BigDecimal(1.25),new ArrayList<Tax>(){{add(new Tax("2","6",new BigDecimal(0.00),new BigDecimal(0.00),new BigDecimal(0.00)));}}));}};
        final Invoice invoice = InvoiceBuilder.createInvoice()
                .issuedBy("Seller Inc.", "Seller Venue", "1710335267001", "Av. Principal", "001", "501", "Av. Amazonas", "12345")
                .issuedWithDetails(details)
                .build();

        assertEquals(details,invoice.getDetails());
    }


    private Date yesterday() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        final Date yesterday = calendar.getTime();
        return yesterday;
    }

    private void assertAreEquals(final String expectedName, final String expectedCommercialName, final String expectedRuc, final String expectedMainAddress,
                                 final boolean expectedToRequireAccounting,final String expectedCommercialVenue,final String expectedPointOfSale,final String expectedCommercialVenueAddress,final String expectedSpecialTaxPayer, final Issuer issuer) {
        assertEquals(expectedName, issuer.getName());
        assertEquals(expectedCommercialName, issuer.getCommercialName());
        assertEquals(expectedRuc, issuer.getRuc());
        assertEquals(expectedMainAddress, issuer.getMainAddress());
        assertEquals(expectedToRequireAccounting, issuer.isAccountingRequired());
        assertEquals(expectedCommercialVenue,issuer.getCommercialVenue());
        assertEquals(expectedPointOfSale,issuer.getPointOfSale());
        assertEquals(expectedCommercialVenueAddress,issuer.getCommercialVenueAddress());
        assertEquals(expectedSpecialTaxPayer,issuer.getSpecialTaxPayer());
    }

    private void assertAreEquals(final String expectedName, final IdentificationType expectedIdType,
                                 final String expectedId, final Customer customer) {
        assertEquals(expectedName, customer.getName());
        assertEquals(expectedIdType, customer.getIdentificationType());
        assertEquals(expectedId, customer.getIdentification());
    }
}
