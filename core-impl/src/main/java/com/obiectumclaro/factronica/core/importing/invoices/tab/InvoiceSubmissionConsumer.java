/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.invoices.tab;

import com.obiectumclaro.factronica.core.enumeration.IdType;
import com.obiectumclaro.factronica.core.enumeration.Person;
import com.obiectumclaro.factronica.core.enumeration.ProductType;
import com.obiectumclaro.factronica.core.importing.ValueValidationException;
import com.obiectumclaro.factronica.core.importing.invoices.InvoiceReporter;
import com.obiectumclaro.factronica.core.importing.invoices.QueryAuthorizationProducer;
import com.obiectumclaro.factronica.core.model.*;
import com.obiectumclaro.factronica.core.service.InvoiceFactoryBean;
import com.obiectumclaro.factronica.core.service.IssuerBean;
import com.obiectumclaro.factronica.core.service.TaxValuesBean;
import com.obiectumclaro.factronica.core.sign.SignerBean;
import com.obiectumclaro.factronica.api.invoice.exception.SignerException;
import com.obiectumclaro.factronica.core.web.service.sri.client.AuthorizationState;
import com.obiectumclaro.factronica.core.web.service.sri.client.InvoiceAuthorization;
import com.obiectumclaro.factronica.core.web.service.sri.client.ServiceEnvironment;
import com.obiectumclaro.factronica.core.web.service.sri.client.reception.Comprobante;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts the imported line into an invoice, signs it and submits it.
 * 
 * @author ipazmino
 * 
 */
@MessageDriven(activationConfig = {
	@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
	@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
	@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/FactronicaInvoiceSubmission") })
public class InvoiceSubmissionConsumer implements MessageListener {

    public static final Logger LOG = Logger.getLogger(InvoiceSubmissionConsumer.class);

    @EJB
    private TaxValuesBean taxValuesBean;
    @EJB
    InvoiceFactoryBean xmlGenerate;
    @EJB
    private IssuerBean issuerBean;
    @EJB
    private SignerBean signer;
    @Inject
    private InvoiceAuthorization authorization;
    @EJB
    private QueryAuthorizationProducer queryAuthorizationProducer;
    @EJB
    private InvoiceReporter invoiceReporter;

    private Issuer issuer;

    @PostConstruct
    public void setupIssuer() {
	setIssuer(issuerBean.getIssuerById(1L));
    }

    @Override
    public void onMessage(final Message queueMessage) {
	final InvoiceSubmissionMessage invoiceSubmission = extractSubmissionFrom(queueMessage);

	try {
	    final InvoiceLine invoiceLine = invoiceSubmission.getInvoice();
	    final Invoice placeHolder = converToInvoice(invoiceLine);
	    final byte[] signedInvoice = signFile(generateXMLFile(placeHolder, invoiceLine));
	    final String customerIdentifier = invoiceLine.getId();
	    requestAuthorization(signedInvoice, customerIdentifier, placeHolder.getAccesKey(), invoiceSubmission);
	    queueForAuthorizationQuery(customerIdentifier, placeHolder.getAccesKey(), invoiceSubmission);
	} catch (SignerException e) {
	    invoiceReporter.recordEntryFor(invoiceSubmission, InvoicingStatus.FAILED_TO_SIGN, e.getMessage());
	} catch (ValueValidationException e) {
	    invoiceReporter.recordEntryFor(invoiceSubmission, InvoicingStatus.FAILED_TO_IMPORT, e.getMessage());
	    LOG.error(e);
	}

    }

    private InvoiceSubmissionMessage extractSubmissionFrom(final Message queueMessage) {
	final ObjectMessage objectMessage;
	try {
	    objectMessage = ObjectMessage.class.cast(queueMessage);
	} catch (final ClassCastException cce) {
	    throw new RuntimeException("Incorrect message type sent to object message consumer; got:"
		    + queueMessage.getClass().getSimpleName(), cce);
	}

	try {
	    final Object obj = objectMessage.getObject();
	    return InvoiceSubmissionMessage.class.cast(obj);
	} catch (final JMSException jmse) {
	    throw new RuntimeException("Could not unwrap JMS Message", jmse);
	} catch (final ClassCastException cce) {
	    throw new RuntimeException("Expected message contents of type " + InvoiceLine.class.getName(), cce);
	}
    }

    private Invoice converToInvoice(final InvoiceLine importedInvoice) {
	Invoice invoice = new Invoice();
	Product product = null;
	InvoiceItem item = null;
	invoice.setCustomerIdNumber(importedInvoice.getId());

	boolean isFirstProd = true;
	for (ProductLine productLine : importedInvoice.getProducst()) {
	    if (productLine.getProduct() != null) {
		product = getProduct(productLine);
		item = getInvoiceItem(productLine);
		if (isFirstProd) {
		    updateDiscount(importedInvoice, product, item);
		    isFirstProd = false;
		}
		item.setProduct(product);
		invoice.addItem(item);
	    }
	}

	invoice.addAdditionalInfo(new AdditionaInformationCustomer("Numero de contrato", importedInvoice
		.getContractNumber()));
	invoice.addAdditionalInfo(new AdditionaInformationCustomer("Fecha maxima de pago", importedInvoice
		.getPaymentDeathline()));
	invoice.addAdditionalInfo(new AdditionaInformationCustomer("Numero de factura fisica", importedInvoice
		.getInvoiceNumber()));

	return invoice;
    }

    private void updateDiscount(final InvoiceLine importedInvoice, Product product, InvoiceItem item) {
	String strDiscount = importedInvoice.getDiscount();
	strDiscount = strDiscount == null || strDiscount.isEmpty() ? "0" : strDiscount;
	BigDecimal discount = new BigDecimal(strDiscount);
	product.setDiscount(discount);
	item.setDiscount(discount);
    }

    private Product getProduct(final ProductLine productLine) {
	Product product = new Product();
	final String code = null == productLine.getCode() || productLine.getCode().isEmpty() ? "Default" : productLine.getCode();
	product.setCode(code);
	product.setName(productLine.getProduct());
	product.setDescription(productLine.getDescription());
	product.setProductType(ProductType.SERVICIO);
	product.setUnitPrice(new BigDecimal(productLine.getPrice()));
	product.setAmount(new BigDecimal(productLine.getAmount()));
	product.setDiscount(BigDecimal.ZERO);
	product.setTotal(product.getUnitPrice().multiply(product.getAmount()));
	List<TaxValue> taxValues = new ArrayList<TaxValue>();
	TaxValue taxValue = taxValuesBean.findByCode("2");
	taxValues.add(taxValue);
	product.setTaxValueList(taxValues);
	return product;
    }

    private InvoiceItem getInvoiceItem(final ProductLine productLine) {
	InvoiceItem item;
	item = new InvoiceItem();
	item.setCode(productLine.getCode());
	item.setName(productLine.getProduct());
	item.setProductType(ProductType.SERVICIO);
	item.setDescription(productLine.getDescription());
	item.setPrice(new BigDecimal(productLine.getPrice()));
	item.setAmount(new BigDecimal(productLine.getAmount()));
	item.setDiscount(BigDecimal.ZERO);
	item.setTotal(new BigDecimal(productLine.getPrice()).multiply(new BigDecimal(productLine.getAmount())));
	return item;
    }

    private byte[] generateXMLFile(final Invoice placeHolder, final InvoiceLine line) throws ValueValidationException {
	final Customer customer = findCustomer(line);
	return xmlGenerate.generateXMLFile(placeHolder, customer, getIssuer());
    }

    private Customer findCustomer(final InvoiceLine line) throws ValueValidationException {
	final Customer customer = new Customer();
	final String id = line.getId();
	customer.setId(id);
	final IdType idType = id.length() == 13 ? IdType.RUC : id.length() == 10 ? IdType.CEDULA : IdType.PASAPORTE;
	customer.setIdType(idType);
	Person person = IdType.RUC.equals(idType) ? Person.JURIDICA : Person.NATURAL;
	customer.setPerson(person);
	customer.setSocialReason(line.getName());
	if (Person.NATURAL.equals(person)) {
	    customer.setName(line.getName());
	}
	customer.setAddress(line.getAddress());
	customer.setPhone(line.getPhone());
	final String email = line.getEmail();
	if (null == email || email.isEmpty()) {
	    throw new ValueValidationException("Es necesaria una direccion de correo para remitir la factura.");
	}
	customer.setEmail(email.split(",")[0]);

	return customer;
    }

    private byte[] signFile(final byte[] xml) throws SignerException {
	return signer.sign(xml, issuer.getCertificate(), issuer.getPassword());
    }

    private void requestAuthorization(final byte[] signedInvoice, final String customerIdentifier,
	    final String accessKey, InvoiceSubmissionMessage invoiceSubmission) {
	final String messagePattern = "%s - %s (%s) %s";

	final AuthorizationState response = authorization.request(signedInvoice, ServiceEnvironment.TEST);
	System.out.println("Access key = " + accessKey + ", response = " + response);

	if (AuthorizationState.DEVUELTA.equals(response)) {
	    final Comprobante comprobante = authorization.getComprobantes().get(0);

	    final StringBuffer message = new StringBuffer();
	    for (com.obiectumclaro.factronica.core.web.service.sri.client.reception.Mensaje mensaje : comprobante
		    .getMensajes().getMensaje()) {
		message.append(String.format(messagePattern, mensaje.getTipo(), mensaje.getMensaje(),
			mensaje.getIdentificador(), mensaje.getInformacionAdicional())
			+ "\n");
	    }

	    invoiceReporter.recordEntryFor(invoiceSubmission, InvoicingStatus.RETURNED, accessKey, message.toString());
	}
    }

    private void queueForAuthorizationQuery(final String customerIdentifier, final String accessKey,
	    final InvoiceSubmissionMessage invoiceSubmission) {
	queryAuthorizationProducer.queueAuthorizationQuery(customerIdentifier, accessKey, invoiceSubmission);
    }

    public void setIssuer(Issuer issuer) {
	this.issuer = issuer;
    }

    public Issuer getIssuer() {
	return issuer;
    }

}
