package com.obiectumclaro.factronica.core.emac.timer;


import com.obiectumclaro.factronica.core.enumeration.DocumentType;
import com.obiectumclaro.factronica.core.importing.invoices.InvoiceReporter;
import com.obiectumclaro.factronica.core.importing.invoices.NotAnswerException;
import com.obiectumclaro.factronica.core.importing.invoices.csv.InvoiceLine;
import com.obiectumclaro.factronica.core.mail.MailMessageBuilder;
import com.obiectumclaro.factronica.core.mail.SMTPMailProducer;
import com.obiectumclaro.factronica.core.mail.model.Attachment;
import com.obiectumclaro.factronica.core.mail.model.MailMessage;
import com.obiectumclaro.factronica.core.model.Document;
import com.obiectumclaro.factronica.core.model.InvoicingStatus;
import com.obiectumclaro.factronica.core.service.InvoiceBean;
import com.obiectumclaro.factronica.core.service.exception.InvoicePrintException;
import com.obiectumclaro.factronica.core.web.service.sri.client.AuthorizationState;
import com.obiectumclaro.factronica.core.web.service.sri.client.InvoiceAuthorization;
import com.obiectumclaro.factronica.core.web.service.sri.client.ServiceEnvironment;
import com.obiectumclaro.factronica.core.web.service.sri.client.authorization.Autorizacion;
import com.obiectumclaro.factronica.core.web.service.sri.client.authorization.Mensaje;
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
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/EmacDocumentAuthorization")})
public class EmacDocumentAuthorizationConsumer implements MessageListener {

    public static final Logger LOG = Logger.getLogger(EmacDocumentAuthorizationConsumer.class);

    @EJB
    private InvoiceReporter invoiceReporter;
    @EJB
    private InvoiceBean invoiceBean;
    @EJB
    private InvoiceAuthorization authorization;
    @EJB
    private SMTPMailProducer mailProducer;
    @Override
    public void onMessage(Message queueMessage) {
        EmacDocumentSubmissionMessage message = extractMessage(queueMessage);
        LOG.info("Inicia consulta de autorizacion");
        final List<Autorizacion> authorizationResponse = authorization.query(message.getAccessKey(), ServiceEnvironment.TEST);
        LOG.info("Finaliza consulta de autorizacion");
        if (authorizationResponse.isEmpty()) {
            throw new NotAnswerException(message.getAccessKey(), ServiceEnvironment.TEST);
        }

        final Autorizacion autorizacion = authorizationResponse.get(0);
        final List<Mensaje> messages = autorizacion.getMensajes().getMensaje();

        final String pattern = "%s - %s (%s) %s";
        StringBuffer messagesInTheResponse = new StringBuffer();
        for (Mensaje mensaje : messages) {
            messagesInTheResponse.append(String.format(pattern, mensaje.getTipo(), mensaje.getMensaje(),
                    mensaje.getIdentificador(), mensaje.getInformacionAdicional())
                    + "\n");
        }

        final String authNumber = autorizacion.getNumeroAutorizacion();
        if ("AUTORIZADO".equals(autorizacion.getEstado())) {
            LOG.info("Finaliza consulta de autorizacion");
            saveAuthorizationDocument(message, autorizacion);
            recordResult(message, InvoicingStatus.AUTORIZED,messagesInTheResponse.toString());

            List<Attachment> attachments;
            try {
                attachments = generateAttachments(autorizacion,message, authNumber);
                sendNotificationToEmail(message, autorizacion, authNumber, attachments);
            } catch (InvoicePrintException e) {
                recordResult(message, InvoicingStatus.FAILED_TO_GENERATE_ATTACHEMETS,e.getMessage());
                LOG.error("access key = " + message.getAccessKey(), e);
            }
        } else {
            recordResult(message, InvoicingStatus.NOT_AUTORIZED,messagesInTheResponse.toString());
        }

    }

    private List<Attachment> generateAttachments(final Autorizacion autorizacion,final EmacDocumentSubmissionMessage message,final String authNumber)
            throws InvoicePrintException {
        List<Attachment> attachments = new ArrayList<Attachment>();
        attachments.add(new Attachment(autorizacion.getComprobante().getBytes(), authNumber + ".xml", "text/xml"));
        attachments.add(new Attachment(invoiceBean.printInvoice(autorizacion, autorizacion.getComprobante(), message.getIssuer()),
                authNumber + ".pdf", "application/pdf"));
        return attachments;
    }

    private void sendNotificationToEmail(final EmacDocumentSubmissionMessage message,
                                         final Autorizacion autorizacion, String authNumber, List<Attachment> attachments) {
        try {
            final String email = message.getSriDocument().getEmail();
            final String emailBody = "Estimado Cliente,\n\nAdjunto enviamos el Documento Electr\u00f3nico Muchas Gracias.\n\n\n\nN\u00famero de Autorizaci\u00f3n: %s Fecha Autorizaci\u00f3n: %s \nClave Acceso: %s";
            final MailMessage multiPartMessage = new MailMessageBuilder()
                    .from(message.getIssuer().getContactMail())
                    .addTo(email)
                    .subject("Facturacion Electronica EMAC-EP")
                    .body(String.format(emailBody, authNumber,
                            autorizacion.getFechaAutorizacion(),message.getAccessKey())).contentType("text/plain").hasAttachment(Boolean.TRUE)
                    .attachments(attachments).build();
            mailProducer.queueMailForDelivery(multiPartMessage);
        } catch (IllegalStateException | IllegalArgumentException e) {
            recordResult(message, InvoicingStatus.FAILED_TO_SEND_EMAIL, e.getMessage());
            LOG.error(e);
        }
    }


    private EmacDocumentSubmissionMessage extractMessage(final Message queueMessage) {
        final ObjectMessage objectMessage;
        try {
            objectMessage = ObjectMessage.class.cast(queueMessage);
        } catch (final ClassCastException cce) {
            throw new RuntimeException("Incorrect message type sent to object message consumer; got:"
                    + queueMessage.getClass().getSimpleName(), cce);
        }
        final EmacDocumentSubmissionMessage queryAuthorizationMessage;
        try {
            final Object obj = objectMessage.getObject();
            queryAuthorizationMessage = EmacDocumentSubmissionMessage.class.cast(obj);
        } catch (final JMSException jmse) {
            throw new RuntimeException("Could not unwrap JMS Message", jmse);
        } catch (final ClassCastException cce) {
            throw new RuntimeException("Expected message contents of type " + InvoiceLine.class.getName(), cce);
        }
        return queryAuthorizationMessage;
    }

    private void saveAuthorizationDocument(final EmacDocumentSubmissionMessage message,
                                           final Autorizacion autorizacion) {
        Document document = new Document();
        document.setAuthorizationNumber(autorizacion.getNumeroAutorizacion());
        document.setAccessKey(message.getAccessKey());
        document.setIssuer(message.getIssuer());
        document.setEmisionDate(new Date());
        document.setDocumentType(DocumentType.FACTURA);
        document.setSignedXml(autorizacion.getComprobante().getBytes());
        document.setState(AuthorizationState.AUTORIZADO);
        invoiceBean.store(document);
    }

    private void recordResult(final EmacDocumentSubmissionMessage message, InvoicingStatus status,String details) {
        invoiceReporter.recordEntryFor(message.getAccessKey(), message.getRequestedOn(), "EMAC", message.getAccessKey(), details,status);
    }
}
