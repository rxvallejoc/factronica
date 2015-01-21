/**
 *
 */
package com.obiectumclaro.factronica.core.emac.timer;

import com.obiectumclaro.factronica.api.invoice.exception.SignerException;
import com.obiectumclaro.factronica.core.emac.access.EmacInvoiceBean;
import com.obiectumclaro.factronica.core.importing.invoices.InvoiceReporter;
import com.obiectumclaro.factronica.core.importing.invoices.tab.InvoiceLine;
import com.obiectumclaro.factronica.core.mail.SMTPMailProducer;
import com.obiectumclaro.factronica.core.mail.SMTPMailService;
import com.obiectumclaro.factronica.core.model.InvoicingStatus;
import com.obiectumclaro.factronica.core.service.InvoiceBean;
import com.obiectumclaro.factronica.core.sign.SignerBean;
import com.obiectumclaro.factronica.core.web.service.sri.client.AuthorizationState;
import com.obiectumclaro.factronica.core.web.service.sri.client.InvoiceAuthorization;
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
    @EJB
    private InvoiceReporter invoiceReporter;
    @EJB
    private EmacDocumentAuthorizationProducer emacDocumentAuthorizationProducer;
    private Autorizacion authInvoice;

    @Override
    public void onMessage(final Message queueMessage) {
        final EmacDocumentSubmissionMessage message = extractSubmissionFrom(queueMessage);
        byte[] signedDocument;
        authInvoice = null;
        try {
            LOG.info("Inicia firma documento");
            signedDocument = signer.sign(message.getDocument(), message.getIssuer().getCertificate(), message.getIssuer().getPassword());
            message.setSignedDocument(signedDocument);
            LOG.info("Finaliza firma documento");


            LOG.info("Inicia envio");
            boolean response = requestAuthorization(message);
            if(response){
                emacDocumentAuthorizationProducer.queueAuthorizationQuery(message);
            }

//            final List<Autorizacion> authResponse = authorization.syncRequest(signedDocument, environment, 15);
//            LOG.info("Finaliza envio");
//            if (!authResponse.isEmpty()) {
//                final Autorizacion autorizacion = authResponse.get(0);
//                final String authorizationStatus = autorizacion.getEstado();
//                LOG.info(String.format("Invoice status: %s", authorizationStatus));
//
//                if (AuthorizationState.AUTORIZADO.name().equals(authorizationStatus)) {
//                    authInvoice = autorizacion.getNumeroAutorizacion() == null ? null : autorizacion;
//                    List<Attachment> attachments = createAttachments(message,autorizacion);
//                    stroreDocument(message, AuthorizationState.AUTORIZADO);
//                    sendMailWithAttachments(message.getSriDocument().getEmail(), attachments);
//                }
//            } else {
//                LOG.info("Existe Problemas con el  Servicio de Rentas Internas ");
//                stroreDocument(message, AuthorizationState.NO_AUTORIZADO);
//            }

//        } catch (ReturnedInvoiceException rie) {
//            final Comprobante comprobante = rie.getComprobantes().get(0);
//            stroreDocument(message, AuthorizationState.NO_AUTORIZADO);
        } catch (SignerException e) {
            LOG.error(e, e);
            invoiceReporter.recordEntryFor(message.getSriDocument().getSecuencial(),message.getRequestedOn(),"EMAC",message.getAccessKey(),message.toString(),InvoicingStatus.RETURNED);
//            stroreDocument(message, AuthorizationState.NO_AUTORIZADO);
        } catch (Exception e) {
            invoiceReporter.recordEntryFor(message.getSriDocument().getSecuencial(),message.getRequestedOn(),"EMAC",message.getAccessKey(),message.toString(),InvoicingStatus.RETURNED);
//            stroreDocument(message, AuthorizationState.NO_AUTORIZADO);
        }
        message.getSriDocument().setEstadoproceso("PA");
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



    private boolean requestAuthorization(EmacDocumentSubmissionMessage message) {
        final String messagePattern = "%s - %s (%s) %s";
        //Cambiar de acuerdo al parametro en la base

        final AuthorizationState response = authorization.request(message.getSignedDocument(),ServiceEnvironment.TEST);

        LOG.info("Access key = " + message.getAccessKey() + ", response = " + response);
        final StringBuffer errorMessage = new StringBuffer();
        if (AuthorizationState.DEVUELTA.equals(response)) {
            LOG.info("Documento Devuelto");
            final Comprobante comprobante = authorization.getComprobantes().get(0);


            for (com.obiectumclaro.factronica.core.web.service.sri.client.reception.Mensaje mensaje : comprobante
                    .getMensajes().getMensaje()) {
                errorMessage.append(String.format(messagePattern, mensaje.getTipo(), mensaje.getMensaje(),
                        mensaje.getIdentificador(), mensaje.getInformacionAdicional())
                        + "\n");
            }
            LOG.info("Existe errores en el XML:" + errorMessage.toString());
            invoiceReporter.recordEntryFor(message.getAccessKey(), message.getRequestedOn(), "EMAC", message.getAccessKey(), errorMessage.toString(), InvoicingStatus.RETURNED);

        }

        if(errorMessage.length()!=0){
            return false;
        }

        return true;
    }


//    private List<Attachment> createAttachments(final EmacDocumentSubmissionMessage message,final Autorizacion autorizacion)
//            throws InvoicePrintException {
//        List<Attachment> attachments = new ArrayList<Attachment>();
//        attachments.add(new Attachment(authInvoice.getComprobante().getBytes(), String.format("%s.xml",
//                authInvoice.getNumeroAutorizacion()), "text/xml"));
//        attachments.add(new Attachment(invoiceBean.printInvoice(autorizacion, authInvoice.getComprobante(), message.getIssuer()), String
//                .format("%s.pdf", authInvoice.getNumeroAutorizacion()), "application/pdf"));
//        return attachments;
//    }

//    private void sendMailWithAttachments(String email, List<Attachment> attachments) {
//        LOG.info("Sending mail ........");
//        final MailMessage multiPartMessage = new MailMessageBuilder()
//                .from("factura@emac.gob.ec")
//                .addTo(email)
//                .subject("Facturacion Electronica")
//                .body(
//                        "Estimado Consumidor, \nAdjunto enviamos su factura electrónica. \n¡Gracias por ser parte de nuestro compromiso con el medio ambiente!\n EMAC-EP ")
//                .contentType("text/plain").hasAttachment(Boolean.TRUE).attachments(attachments).build();
//        mailProducer.queueMailForDelivery(multiPartMessage);
//        LOG.info("Mail sended ........");
//    }

//    private void stroreDocument(EmacDocumentSubmissionMessage message, AuthorizationState auth) {
//        Document document = new Document();
//        document.setAuthorizationNumber(authInvoice != null ? authInvoice.getNumeroAutorizacion() : "");
//        document.setAccessKey(message.getAccessKey());
//        document.setIssuer(message.getIssuer());
//        document.setEmisionDate(new Date());
//        document.setDocumentType(DocumentType.FACTURA);
//        document.setSignedXml(message.getSignedDocument());
//        document.setState(auth);
//        invoiceBean.store(document);
//    }


}
