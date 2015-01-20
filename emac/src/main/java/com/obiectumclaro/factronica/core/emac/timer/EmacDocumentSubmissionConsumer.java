/**
 *
 */
package com.obiectumclaro.factronica.core.emac.timer;

import com.obiectumclaro.factronica.api.invoice.exception.SignerException;
import com.obiectumclaro.factronica.core.emac.access.EmacInvoiceBean;
import com.obiectumclaro.factronica.core.enumeration.DocumentType;
import com.obiectumclaro.factronica.core.importing.invoices.tab.InvoiceLine;
import com.obiectumclaro.factronica.core.mail.MailMessageBuilder;
import com.obiectumclaro.factronica.core.mail.SMTPMailProducer;
import com.obiectumclaro.factronica.core.mail.SMTPMailService;
import com.obiectumclaro.factronica.core.mail.model.Attachment;
import com.obiectumclaro.factronica.core.mail.model.MailMessage;
import com.obiectumclaro.factronica.core.model.Document;
import com.obiectumclaro.factronica.core.service.InvoiceBean;
import com.obiectumclaro.factronica.core.service.exception.InvoicePrintException;
import com.obiectumclaro.factronica.core.sign.SignerBean;
import com.obiectumclaro.factronica.core.web.service.sri.client.AuthorizationState;
import com.obiectumclaro.factronica.core.web.service.sri.client.InvoiceAuthorization;
import com.obiectumclaro.factronica.core.web.service.sri.client.ReturnedInvoiceException;
import com.obiectumclaro.factronica.core.web.service.sri.client.ServiceEnvironment;
import com.obiectumclaro.factronica.core.web.service.sri.client.authorization.Autorizacion;
import com.obiectumclaro.factronica.core.web.service.sri.client.reception.Comprobante;
import org.apache.log4j.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/EmacDocumentSubmission")})
public class EmacDocumentSubmissionConsumer implements MessageListener {

    public static final Logger LOG = Logger.getLogger(EmacDocumentSubmissionConsumer.class);

    @EJB
    private SignerBean signer;
    @EJB
    private InvoiceAuthorization authorization;
    @EJB
    private SMTPMailProducer mailProducer;
    @EJB
    private SMTPMailService mailService;
    @EJB
    private InvoiceBean invoiceBean;
    @EJB
    private EmacInvoiceBean emacInvoiceBean;
    private Autorizacion authInvoice;

    @Override
    public void onMessage(final Message queueMessage) {
        final EmacDocumentSubmissionMessage message = extractSubmissionFrom(queueMessage);
        byte[] signedDocument;
        authInvoice = null;
        try {
            signedDocument = signer.sign(message.getDocument(), message.getIssuer().getCertificate(), message.getIssuer().getPassword());
            message.setSignedDocument(signedDocument);
            //Cambiar de acuerdo al parametro en la base
            ServiceEnvironment environment;
            if (message.getIssuer().getEnvironment().equals("PRUEBAS")) {
                environment = ServiceEnvironment.TEST;
            } else {
                environment = ServiceEnvironment.PRODUCTION;
            }
            final List<Autorizacion> authResponse = authorization.syncRequest(signedDocument, environment, 15);
            if (!authResponse.isEmpty()) {
                final Autorizacion autorizacion = authResponse.get(0);
                final String authorizationStatus = autorizacion.getEstado();
                LOG.info(String.format("Invoice status: %s", authorizationStatus));

                if (AuthorizationState.AUTORIZADO.name().equals(authorizationStatus)) {
                    authInvoice = autorizacion.getNumeroAutorizacion() == null ? null : autorizacion;
                    List<Attachment> attachments = createAttachments(message,autorizacion);
                    stroreDocument(message, AuthorizationState.AUTORIZADO);
                    sendMailWithAttachments(message.getSriDocument().getEmail(), attachments);
                }
            } else {
                LOG.info("Existe Problemas con el  Servicio de Rentas Internas ");
                stroreDocument(message, AuthorizationState.NO_AUTORIZADO);
            }

        } catch (ReturnedInvoiceException rie) {
            final Comprobante comprobante = rie.getComprobantes().get(0);
            stroreDocument(message, AuthorizationState.NO_AUTORIZADO);
        } catch (SignerException e) {
            LOG.error(e, e);
            stroreDocument(message, AuthorizationState.NO_AUTORIZADO);
        } catch (Exception e) {
            stroreDocument(message, AuthorizationState.NO_AUTORIZADO);
        }
        message.getSriDocument().setEstadoproceso("GE");
        emacInvoiceBean.update(message.getSriDocument());

    }

    private EmacDocumentSubmissionMessage extractSubmissionFrom(final Message queueMessage) {
        final ObjectMessage objectMessage;
        try {
            objectMessage = ObjectMessage.class.cast(queueMessage);
        } catch (final ClassCastException cce) {
            throw new RuntimeException("Incorrect message type sent to object message consumer; got:"
                    + queueMessage.getClass().getSimpleName(), cce);
        }

        try {
            final Object obj = objectMessage.getObject();
            return EmacDocumentSubmissionMessage.class.cast(obj);
        } catch (final JMSException jmse) {
            throw new RuntimeException("Could not unwrap JMS Message", jmse);
        } catch (final ClassCastException cce) {
            throw new RuntimeException("Expected message contents of type " + InvoiceLine.class.getName(), cce);
        }
    }



    private void requestAuthorization(EmacDocumentSubmissionMessage document) {
        final String messagePattern = "%s - %s (%s) %s";

        final AuthorizationState response = authorization.request(document.getSignedDocument(), ServiceEnvironment.TEST);
        System.out.println("Access key = " + document.getAccessKey() + ", response = " + response);

        if (AuthorizationState.DEVUELTA.equals(response)) {
            final Comprobante comprobante = authorization.getComprobantes().get(0);

            final StringBuffer message = new StringBuffer();
            for (com.obiectumclaro.factronica.core.web.service.sri.client.reception.Mensaje mensaje : comprobante
                    .getMensajes().getMensaje()) {
                message.append(String.format(messagePattern, mensaje.getTipo(), mensaje.getMensaje(),
                        mensaje.getIdentificador(), mensaje.getInformacionAdicional())
                        + "\n");
            }

            //invoiceReporter.recordEntryFor(invoiceSubmission, InvoicingStatus.RETURNED, accessKey, message.toString());
        }
    }


    private List<Attachment> createAttachments(final EmacDocumentSubmissionMessage message,final Autorizacion autorizacion)
            throws InvoicePrintException {
        List<Attachment> attachments = new ArrayList<Attachment>();
        attachments.add(new Attachment(authInvoice.getComprobante().getBytes(), String.format("%s.xml",
                authInvoice.getNumeroAutorizacion()), "text/xml"));
        attachments.add(new Attachment(invoiceBean.printInvoice(autorizacion, authInvoice.getComprobante(), message.getIssuer()), String
                .format("%s.pdf", authInvoice.getNumeroAutorizacion()), "application/pdf"));
        return attachments;
    }

    private void sendMailWithAttachments(String email, List<Attachment> attachments) {
        LOG.info("Sending mail ........");
        final MailMessage multiPartMessage = new MailMessageBuilder()
                .from("notificaciones@hidrocentro.net")
                .addTo(email)
                .subject("Facturacion Electronica")
                .body(
                        "Estimado Consumidor, \nAdjunto enviamos su factura electrónica. \n¡Gracias por ser parte de nuestro compromiso con el medio ambiente!\n EMAC-EP ")
                .contentType("text/plain").hasAttachment(Boolean.TRUE).attachments(attachments).build();
        mailProducer.queueMailForDelivery(multiPartMessage);
        LOG.info("Mail sended ........");
    }

    private void stroreDocument(EmacDocumentSubmissionMessage message, AuthorizationState auth) {
        Document document = new Document();
        document.setAuthorizationNumber(authInvoice != null ? authInvoice.getNumeroAutorizacion() : "");
        document.setAccessKey(message.getAccessKey());
        document.setIssuer(message.getIssuer());
        document.setEmisionDate(new Date());
        document.setDocumentType(DocumentType.FACTURA);
        document.setSignedXml(message.getSignedDocument());
        document.setState(auth);
        invoiceBean.store(document);
    }


}
