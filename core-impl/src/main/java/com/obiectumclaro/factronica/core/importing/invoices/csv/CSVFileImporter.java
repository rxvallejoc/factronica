/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.importing.invoices.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;

import com.obiectumclaro.factronica.core.importing.ValueValidationException;
import com.obiectumclaro.factronica.core.importing.invoices.QueryAuthorizationProducer;
import com.obiectumclaro.factronica.core.model.AdditionaInformationCustomer;
import com.obiectumclaro.factronica.core.model.Customer;
import com.obiectumclaro.factronica.core.model.DocumentReport;
import com.obiectumclaro.factronica.core.model.Invoice;
import com.obiectumclaro.factronica.core.model.InvoiceItem;
import com.obiectumclaro.factronica.core.model.Issuer;
import com.obiectumclaro.factronica.core.model.Product;
import com.obiectumclaro.factronica.core.model.access.ProductEaoBean;
import com.obiectumclaro.factronica.core.service.AgreementsBean;
import com.obiectumclaro.factronica.core.service.CustomersBean;
import com.obiectumclaro.factronica.core.service.DocumentReportBean;
import com.obiectumclaro.factronica.core.service.InvoiceFactoryBean;
import com.obiectumclaro.factronica.core.service.IssuerBean;
import com.obiectumclaro.factronica.core.sign.SignerBean;
import com.obiectumclaro.factronica.api.invoice.exception.SignerException;
import com.obiectumclaro.factronica.core.web.service.sri.client.AuthorizationState;
import com.obiectumclaro.factronica.core.web.service.sri.client.InvoiceAuthorization;
import com.obiectumclaro.factronica.core.web.service.sri.client.ServiceEnvironment;
import com.obiectumclaro.factronica.core.web.service.sri.client.reception.Comprobante;

/**
 * 
 * @author marco
 */
@Stateful
public class CSVFileImporter {

    public static final String DELIMITADOR = ",";

    @Inject
    InvoiceFactoryBean xmlGenerate;
    @Inject
    private SignerBean signer;
    @Inject
    private InvoiceAuthorization authorization;
    @Inject
    AgreementsBean agreementsBean;
    @Inject
    IssuerBean issuerBean;
    @Inject
    ProductEaoBean productBean;
    @Inject
    CustomersBean custommerBean;
    @EJB
    private DocumentReportBean documentReportBean;
    @Inject
    private QueryAuthorizationProducer queryAuthProducer;

    private List<InvoiceLine> lines;
    private Issuer issuer;

    @PostConstruct
    public void setupIssuer() {
	setIssuer(issuerBean.getIssuerById(1L));
    }

    public List<InvoiceLine> readCSVFile(final InputStream csv) {
	lines = new ArrayList<>();
	String line = null;
	String[] tokens = null;

	try (BufferedReader reader = new BufferedReader(new InputStreamReader(csv))) {
	    while ((line = reader.readLine()) != null) {
		tokens = line.split(DELIMITADOR);
		lines.add(InvoiceLine.buildLine(tokens));
	    }
	} catch (IOException e) {
	    throw new RuntimeException("Failed to read line in file", e);
	}
	return lines;
    }

    public int importInvoices(String csvName) {
	final Map<InvoiceHeader, List<InvoiceDetail>> detailsPerHeader = mapDetailsPerHeader();
	final Set<InvoiceHeader> keys = detailsPerHeader.keySet();

	Invoice placeHolder = null;
	byte[] signedInvoice = null;

	for (InvoiceHeader header : keys) {
	    try {
		placeHolder = convertToInvoice(header, detailsPerHeader.get(header));

		signedInvoice = signFile(generateXMLFile(placeHolder));
		requestAuthorization(signedInvoice, header.getLineNumber(), placeHolder.getAccesKey());
	    } catch (ValueValidationException vve) {
		lines.get(header.getLineNumber()).addErrorMessage(vve.getMessage());
		System.err.println(vve.getMessage());
		continue;
	    } catch (SignerException e) {
		lines.get(header.getLineNumber()).addErrorMessage(e.getMessage());
		System.err.println(e.getMessage());
		continue;
	    }
	}

	int counter = 0;
	for (InvoiceLine line : lines) {
	    if (LineStatus.RECIBIDA.equals(line.getStatus())) {
		queryAuthorizationResponse(line);
		counter++;
	    }
	}
	final String reportName = "report-" + csvName;
	System.out.println("BEGIN " + reportName);
	final List<InvoiceLine> stream = getReport();

	for (InvoiceLine invoiceLine : stream) {
	    DocumentReport documentReport = new DocumentReport();
	    documentReport.setArchive(reportName);
	    documentReport.setDate(new Date());
	    documentReport.setLine(invoiceLine.toString());
	    documentReportBean.store(documentReport);
	    System.out.println(invoiceLine);
	}
	System.out.println("END " + reportName);
	return counter;
    }

    public List<InvoiceLine> getReport() {
	return lines;
    }

    private Map<InvoiceHeader, List<InvoiceDetail>> mapDetailsPerHeader() {
	final Map<InvoiceHeader, List<InvoiceDetail>> detailsPerHeader = new HashMap<>();
	for (InvoiceLine line : lines) {
	    System.out.println("line = " + line);
	    if (LineStatus.ENVIADA.equals(line.getStatus())) {
		InvoiceHeader header = new InvoiceHeader(line, lines.lastIndexOf(line));
		InvoiceDetail detail = new InvoiceDetail(line, lines.lastIndexOf(line));
		if (!detailsPerHeader.containsKey(header)) {
		    List<InvoiceDetail> listaDetalles = new ArrayList<>();
		    listaDetalles.add(detail);
		    detailsPerHeader.put(header, listaDetalles);
		} else {
		    detailsPerHeader.get(header).add(detail);
		}
	    }
	}
	return detailsPerHeader;
    }

    private Invoice convertToInvoice(final InvoiceHeader header, List<InvoiceDetail> details)
	    throws ValueValidationException {
	Invoice invoice = new Invoice();
	Product product = null;
	InvoiceItem item = null;

	invoice.setCustomerIdNumber(header.getNumeroIdentificacion());

	for (InvoiceDetail detail : details) {
	    product = findProduct(detail.getCodigoProducto());
	    product.setAmount(detail.getCantidad());
	    product.setDiscount(BigDecimal.ZERO);
	    item = new InvoiceItem(product);
	    item.setAmount(detail.getCantidad());
	    item.setDiscount(BigDecimal.ZERO);
	    item.setTotal(product.getUnitPrice().multiply(product.getAmount()));
	    for (String key : detail.getInfoAdicional().keySet()) {
		invoice.addAdditionalInfo(new AdditionaInformationCustomer(key, detail.getInfoAdicional().get(key)));
	    }
	    invoice.addItem(item);
	}

	System.out.println("Total added items = " + invoice.getItems().size());
	return invoice;
    }

    private Product findProduct(final String codigoProducto) throws ValueValidationException {
	final Product product = productBean.findByCode(codigoProducto);
	if (null == product) {
	    final String message = "No existe un producto con el codigo " + codigoProducto;
	    throw new ValueValidationException(message);
	}
	return product;
    }

    private Customer findCustomer(final String identificacion) throws ValueValidationException {
	final Customer customer = custommerBean.findById(identificacion);
	if (null == customer) {
	    final String message = "No existe un cliente con el numero de identificacion " + identificacion;
	    throw new ValueValidationException(message);
	}
	return customer;
    }

    private byte[] generateXMLFile(final Invoice placeHolder) throws ValueValidationException {
	final Customer customer = findCustomer(placeHolder.getCustomerIdNumber());
	return xmlGenerate.generateXMLFile(placeHolder, customer, getIssuer());
    }

    private byte[] signFile(final byte[] xml) throws SignerException {
	return signer.sign(xml, issuer.getCertificate(), issuer.getPassword());
    }

    private void requestAuthorization(final byte[] signedInvoice, final int lineNumber, final String accessKey) {
	final AuthorizationState response = authorization.request(signedInvoice, ServiceEnvironment.TEST);
	lines.get(lineNumber).addMessage(response.name());
	lines.get(lineNumber).setStatus(LineStatus.valueOf(response.name()));
	lines.get(lineNumber).setAccessKey(accessKey);
	System.out.println("Access key = " + accessKey + ", response = " + response);
	if (AuthorizationState.DEVUELTA.equals(response)) {
	    final Comprobante comprobante = authorization.getComprobantes().get(0);
	    final String pattern = "%s - %s (%s) %s";
	    String message = null;
	    for (com.obiectumclaro.factronica.core.web.service.sri.client.reception.Mensaje mensaje : comprobante
		    .getMensajes().getMensaje()) {
		message = String.format(pattern, mensaje.getTipo(), mensaje.getMensaje(), mensaje.getIdentificador(),
			mensaje.getInformacionAdicional());
		lines.get(lineNumber).addMessage(message);
	    }
	}
    }

    private void queryAuthorizationResponse(final InvoiceLine line) {
	queryAuthProducer.queueAuthorizationQuery(line.getId(), line.getAccessKey());
    }

    /**
     * @return the issuer
     */
    public Issuer getIssuer() {
	return issuer;
    }

    /**
     * @param issuer
     *            the issuer to set
     */
    public void setIssuer(Issuer issuer) {
	this.issuer = issuer;
    }
}
