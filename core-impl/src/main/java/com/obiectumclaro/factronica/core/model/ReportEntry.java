/**
 * 
 */
package com.obiectumclaro.factronica.core.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author ipazmino
 *
 */
@Entity
public class ReportEntry implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    private String requestor;
    private Date requestedOn;
    private Date processedOn = Calendar.getInstance().getTime();
    private String invoiceNumber;
    private String cusotmerLogin;
    @Enumerated(EnumType.STRING)
    private InvoicingStatus invoicingStatus;
    private String accessKey;
    @Column(name = "details", length = 2048)
    private String details;
    
    public ReportEntry(String requestor, Date requestedOn, String invoiceNumber, String cusotmerLogin,
	    InvoicingStatus invoicingStatus, String details) {
	this(requestor, requestedOn, invoiceNumber, cusotmerLogin, null, invoicingStatus, details);
    }

    public ReportEntry(String requestor, Date requestedOn, String invoiceNumber, String cusotmerLogin,
	    String accessKey, InvoicingStatus invoicingStatus, String details) {
	this.requestor = requestor;
	this.requestedOn = requestedOn;
	this.invoiceNumber = invoiceNumber;
	this.cusotmerLogin = cusotmerLogin;
	this.accessKey = accessKey;
	this.invoicingStatus = invoicingStatus;
	this.details = details;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public Date getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(Date requestedOn) {
        this.requestedOn = requestedOn;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCusotmerLogin() {
        return cusotmerLogin;
    }

    public void setCusotmerLogin(String cusotmerLogin) {
        this.cusotmerLogin = cusotmerLogin;
    }

    public InvoicingStatus getInvoicingStatus() {
        return invoicingStatus;
    }

    public void setInvoicingStatus(InvoicingStatus invoicingStatus) {
        this.invoicingStatus = invoicingStatus;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public Date getProcessedOn() {
        return processedOn;
    }

    public void setProcessedOn(Date processedOn) {
        this.processedOn = processedOn;
    }

}
