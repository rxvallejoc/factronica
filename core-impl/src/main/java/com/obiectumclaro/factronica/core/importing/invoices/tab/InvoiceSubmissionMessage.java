/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.invoices.tab;

import java.io.Serializable;
import java.util.Date;

/**
 * This is the message to be queued for the invoice to be submitted.
 * 
 * @author ipazmino
 * 
 */
public class InvoiceSubmissionMessage implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private InvoiceLine invoice;
    private String requestor;
    private Date requestedOn;

    public InvoiceSubmissionMessage(final InvoiceLine invoice, final String requestor, final Date requestedOn) {
	this.invoice = invoice;
	this.requestor = requestor;
	this.requestedOn = requestedOn;
    }

    public InvoiceLine getInvoice() {
	return invoice;
    }

    public void setInvoice(InvoiceLine invoice) {
	this.invoice = invoice;
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
}
