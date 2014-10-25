/**
 * 
 */
package com.obiectumclaro.factronica.core.service;

import com.obiectumclaro.factronica.core.enumeration.DocumentType;
import com.obiectumclaro.factronica.core.mail.MailMessageBuilder;
import com.obiectumclaro.factronica.core.mail.SMTPMailProducer;
import com.obiectumclaro.factronica.core.mail.SMTPMailService;
import com.obiectumclaro.factronica.core.mail.model.Attachment;
import com.obiectumclaro.factronica.core.mail.model.MailMessage;
import com.obiectumclaro.factronica.core.model.Customer;
import com.obiectumclaro.factronica.core.model.Document;
import com.obiectumclaro.factronica.core.model.Invoice;
import com.obiectumclaro.factronica.core.model.Issuer;
import com.obiectumclaro.factronica.core.service.exception.InvoicePrintException;
import com.obiectumclaro.factronica.core.sign.SignerBean;
import com.obiectumclaro.factronica.api.invoice.exception.SignerException;
import com.obiectumclaro.factronica.core.web.service.sri.client.InvoiceAuthorization;
import com.obiectumclaro.factronica.core.web.service.sri.client.ReturnedInvoiceException;
import com.obiectumclaro.factronica.core.web.service.sri.client.ServiceEnvironment;
import com.obiectumclaro.factronica.core.web.service.sri.client.authorization.Autorizacion;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author rvallejo
 *
 */
@Stateless
public class IssuerInvoiceBean {
	@EJB
	private SignerBean signer;
    @EJB
    private InvoiceAuthorization authorization;
    @EJB
    private InvoiceBean invoiceBean;
    @EJB
    private SMTPMailProducer mailProducer;
    @EJB
    private SMTPMailService mailService;
    @EJB
    private InvoiceFactoryBean xmlGenerate;
    
	
	public void send(final Issuer issuer,final Customer customer,final Invoice invoice) throws SignerException, ReturnedInvoiceException, InvoicePrintException{
		byte[] signedInvoice=signer.sign(xmlGenerate.generateXMLFile(invoice, customer, issuer),issuer.getCertificate(),issuer.getPassword());
		final List<Autorizacion> authResponse = authorization.syncRequest(signedInvoice, ServiceEnvironment.TEST, 5);
        final Autorizacion authorizationResponse = authResponse.get(0);
        storeDocument(issuer, invoice, authorizationResponse, signedInvoice);
        sendMail("notificaciones@trans-telco.com", customer.getEmail(),  createAttachments(authorizationResponse,issuer));
	}
	
	private List<Attachment> createAttachments(final Autorizacion authorization,Issuer issuer) throws InvoicePrintException{
		  List<Attachment> attachments = new ArrayList<Attachment>();
          attachments.add(new Attachment(authorization.getComprobante().getBytes(), String.format("%s.xml",
        		  authorization.getNumeroAutorizacion()), "text/xml")); 
          attachments.add(new Attachment(invoiceBean.printInvoice(authorization, authorization.getComprobante(),issuer), String
              .format("%s.pdf", authorization.getNumeroAutorizacion()), "application/pdf"));
		return attachments;
	}
	
	private void storeDocument(final Issuer issuer,final Invoice invoice,final Autorizacion authorization,final byte[] signedInvoice){
		Document document=new Document();
        document.setAuthorizationNumber(authorization.getNumeroAutorizacion());
        document.setAccessKey(invoice.getAccesKey());
        document.setIssuer(issuer);
        document.setEmisionDate(new Date());
        document.setDocumentType(DocumentType.FACTURA);
        document.setSignedXml(signedInvoice);
        invoiceBean.store(document);
	}
	
	private void sendMail(final String issuerMail,final String customerMail,final List<Attachment> attachments){
		final MailMessage multiPartMessage = new MailMessageBuilder()
        .from(issuerMail)
        .addTo(customerMail)
        .subject("Facturacion Electronica")
        .body(
            "Estimado Consumidor, \nAdjunto enviamos su factura electrónica. \n¡Gracias por ser parte de nuestro compromiso con el medio ambiente!\n IntegralData ")
        .contentType("text/plain").hasAttachment(Boolean.TRUE).attachments(attachments).build();
    mailProducer.queueMailForDelivery(multiPartMessage);
		
	}
}
