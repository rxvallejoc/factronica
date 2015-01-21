/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.invoices;

import com.obiectumclaro.factronica.core.importing.invoices.tab.InvoiceLine;
import com.obiectumclaro.factronica.core.importing.invoices.tab.InvoiceSubmissionMessage;
import com.obiectumclaro.factronica.core.model.InvoicingStatus;
import com.obiectumclaro.factronica.core.model.ReportEntry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

/**
 * @author ipazmino
 * 
 */
@Stateless
public class InvoiceReporter {

    @PersistenceContext
    private EntityManager entityManager;

    public void recordEntryFor(InvoiceSubmissionMessage invoiceSubmission, InvoicingStatus invoicingStatus,
	    String details) {
	InvoiceLine invoice = invoiceSubmission.getInvoice();
	final ReportEntry reportEntry = new ReportEntry(invoiceSubmission.getRequestor(),
		invoiceSubmission.getRequestedOn(), invoice.getInvoiceNumber(), invoice.getCusotmerLogin(),
		invoicingStatus, details);

	entityManager.persist(reportEntry);
    }

    public void recordEntryFor(InvoiceSubmissionMessage invoiceSubmission, InvoicingStatus invoicingStatus,
	    String accessKey, String details) {
	InvoiceLine invoice = invoiceSubmission.getInvoice();
	final ReportEntry reportEntry = new ReportEntry(invoiceSubmission.getRequestor(),
		invoiceSubmission.getRequestedOn(), invoice.getInvoiceNumber(), invoice.getCusotmerLogin(), accessKey,
		invoicingStatus, details);

	entityManager.persist(reportEntry);
    }

	public void recordEntryFor(String invoiceNumber,Date requestOn,String requestor,String accessKey, String details,InvoicingStatus invoicingStatus){
		final ReportEntry reportEntry = new ReportEntry(requestor,
				requestOn, invoiceNumber, "EMAC", accessKey,
				invoicingStatus, details);
		entityManager.persist(reportEntry);
	}

}
