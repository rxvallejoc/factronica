/**
 *
 */
package com.obiectumclaro.factronica.core.emac.timer;

import com.obiectumclaro.factronica.api.invoice.exception.SignerException;
import com.obiectumclaro.factronica.core.emac.access.EmacInvoiceBean;
import com.obiectumclaro.factronica.core.enumeration.DocumentType;
import com.obiectumclaro.factronica.core.importing.invoices.InvoiceReporter;
import com.obiectumclaro.factronica.core.importing.invoices.tab.InvoiceLine;
import com.obiectumclaro.factronica.core.mail.SMTPMailProducer;
import com.obiectumclaro.factronica.core.mail.SMTPMailService;
import com.obiectumclaro.factronica.core.model.Document;
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
import java.util.Date;


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

            boolean response = requestAuthorization(message);
            if(response){
                LOG.info("Inicia envio a consultar autorizacion");
                emacDocumentAuthorizationProducer.queueAuthorizationQuery(message);
                LOG.info("Inicia envio a consultar autorizacion");
            }

        } catch (SignerException e) {
            LOG.error(e, e);
            invoiceReporter.recordEntryFor(message.getSriDocument().getSecuencial(),message.getRequestedOn(),"EMAC",message.getAccessKey(),message.toString(),InvoicingStatus.RETURNED);
            stroreDocument(message, AuthorizationState.NO_AUTORIZADO);
        } catch (Exception e) {
            invoiceReporter.recordEntryFor(message.getSriDocument().getSecuencial(),message.getRequestedOn(),"EMAC",message.getAccessKey(),message.toString(),InvoicingStatus.RETURNED);
            stroreDocument(message, AuthorizationState.NO_AUTORIZADO);
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
