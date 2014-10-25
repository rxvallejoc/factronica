/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.invoices;

import java.io.Serializable;

import com.obiectumclaro.factronica.core.importing.invoices.tab.InvoiceSubmissionMessage;

/**
 * This is the message to be queued for the query authorization to be submitted.
 * 
 * @author ipazmino
 * 
 */
public class QueryAuthorizationMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String customerIdentifier;
    private String accessKey;
    private InvoiceSubmissionMessage invoiceSubmission;

    public QueryAuthorizationMessage(final String customerIdentifier, final String accessKey) {
	this(customerIdentifier, accessKey, null);
    }

    public QueryAuthorizationMessage(String customerIdentifier, String accessKey,
	    InvoiceSubmissionMessage invoiceSubmission) {
	this.customerIdentifier = customerIdentifier;
	this.accessKey = accessKey;
	this.invoiceSubmission = invoiceSubmission;
    }

    public String getCustomerIdentifier() {
	return customerIdentifier;
    }

    public void setCustomerIdentifier(String customerIdentifier) {
	this.customerIdentifier = customerIdentifier;
    }

    public String getAccessKey() {
	return accessKey;
    }

    public void setAccessKey(String accessKey) {
	this.accessKey = accessKey;
    }

    public InvoiceSubmissionMessage getInvoiceSubmission() {
	return invoiceSubmission;
    }

    public void setInvoiceSubmission(InvoiceSubmissionMessage invoiceSubmission) {
	this.invoiceSubmission = invoiceSubmission;
    }

}
