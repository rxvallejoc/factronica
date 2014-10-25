package com.obiectumclaro.factronica.api.invoice.domain;

import com.obiectumclaro.factronica.api.invoice.enumeration.Document;
import com.obiectumclaro.factronica.api.invoice.enumeration.Environment;
import com.obiectumclaro.factronica.api.invoice.enumeration.IssuingMode;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Invoice {

    private Issuer issuer;
    private Customer customer;
    private Date dateOfIssue;
    private Environment environment;
    private IssuingMode issuingMode;
    private Document documentType;
    private String sequenceNumber;
    private String accessKey;
    private String currency;
    private BigDecimal totalWithoutTax;
    private BigDecimal totalDiscount;
    private BigDecimal totalAmount;
    private BigDecimal tip;
    private List<TotalTax> totalTaxes;
    private List<Item> details;
    private List<InvoiceRetention> invoiceRetentions;
    private List<AdditionalInformation> additionalInformations;

    public Issuer getIssuer() {
        return issuer;
    }

    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setIssuingMode(IssuingMode issuingMode) {
        this.issuingMode = issuingMode;
    }

    public IssuingMode getIssuingMode() {
        return issuingMode;
    }

    public void setDocumentType(Document documentCode) {
        this.documentType = documentCode;
    }

    public Document getDocumentType() {
        return documentType;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setTotalWithoutTax(BigDecimal totalWithoutTax) {
        this.totalWithoutTax = totalWithoutTax;
    }

    public BigDecimal getTotalWithoutTax() {
        return totalWithoutTax;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTip(BigDecimal tip) {
        this.tip = tip;
    }

    public BigDecimal getTip() {
        return tip;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setTotalTaxes(List<TotalTax> totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public List<TotalTax> getTotalTaxes() {
        return totalTaxes;
    }

    public void setDetails(List<Item> details) {
        this.details = details;
    }

    public List<Item> getDetails() {
        return details;
    }

    public List<InvoiceRetention> getInvoiceRetentions() {
        return invoiceRetentions;
    }

    public void setInvoiceRetentions(List<InvoiceRetention> invoiceRetentions) {
        this.invoiceRetentions = invoiceRetentions;
    }
}
