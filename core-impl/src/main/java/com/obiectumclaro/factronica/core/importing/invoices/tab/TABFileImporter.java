/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.invoices.tab;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.obiectumclaro.factronica.core.model.User;
import com.obiectumclaro.file.reader.AnnotatedFilePlainReader;
import com.obiectumclaro.file.reader.FileReader;
import com.obiectumclaro.file.reader.exception.FileReadingException;

/**
 * @author rvallejo
 * 
 */
@Stateless
public class TABFileImporter {

    public static final Logger LOG = Logger.getLogger(TABFileImporter.class);

    @Inject
    private InvoiceSubmissionProducer invoiceSubmitter;


    public List<InvoiceLine> readTABFile(final byte[] file) {
	InvoiceFileExecutor invoiceFileExecutor = new InvoiceFileExecutor();
	FileReader fileReader = new AnnotatedFilePlainReader<InvoiceLine>(file, invoiceFileExecutor, InvoiceLine.class);
	try {
	    fileReader.read();
	    LOG.info(String.format("File size: %s", invoiceFileExecutor.getInvoiceFiles().size()));
	} catch (FileReadingException e) {
	    LOG.error(e);
	}
	return invoiceFileExecutor.getInvoiceFiles();
    }

    @Asynchronous
    public void importInvoices(final List<InvoiceLine> invoices, final User requester) {
	final Calendar cal = Calendar.getInstance();

	for (InvoiceLine invoiceLine : invoices) {
	    InvoiceSubmissionMessage message = new InvoiceSubmissionMessage(invoiceLine, requester.getUsername(),cal.getTime());
	    invoiceSubmitter.queueInvoiceSubmission(message);
	}
    }

}
