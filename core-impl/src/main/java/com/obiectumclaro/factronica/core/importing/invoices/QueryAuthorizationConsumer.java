/**
 *
 */
package com.obiectumclaro.factronica.core.importing.invoices;

import com.obiectumclaro.factronica.core.enumeration.DocumentType;
import com.obiectumclaro.factronica.core.importing.invoices.csv.InvoiceLine;
import com.obiectumclaro.factronica.core.mail.MailMessageBuilder;
import com.obiectumclaro.factronica.core.mail.SMTPMailProducer;
import com.obiectumclaro.factronica.core.mail.model.Attachment;
import com.obiectumclaro.factronica.core.mail.model.MailMessage;
import com.obiectumclaro.factronica.core.model.Document;
import com.obiectumclaro.factronica.core.model.InvoicingStatus;
import com.obiectumclaro.factronica.core.model.Issuer;
import com.obiectumclaro.factronica.core.service.AgreementsBean;
import com.obiectumclaro.factronica.core.service.CustomersBean;
import com.obiectumclaro.factronica.core.service.InvoiceBean;
import com.obiectumclaro.factronica.core.service.IssuerBean;
import com.obiectumclaro.factronica.core.service.exception.InvoicePrintException;
import com.obiectumclaro.factronica.core.web.service.sri.client.AuthorizationState;
import com.obiectumclaro.factronica.core.web.service.sri.client.InvoiceAuthorization;
import com.obiectumclaro.factronica.core.web.service.sri.client.ServiceEnvironment;
import com.obiectumclaro.factronica.core.web.service.sri.client.authorization.Autorizacion;
import com.obiectumclaro.factronica.core.web.service.sri.client.authorization.Mensaje;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author ipazmino
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = QueryAuthorizationConstants.JNDI_QUERY_AUTHORIZATION_QUEUE)})
public class QueryAuthorizationConsumer implements MessageListener {

    private static Logger LOG = Logger.getLogger(QueryAuthorizationConsumer.class);

    @Inject
    IssuerBean issuerBean;
    @Inject
    CustomersBean custommerBean;
    @Inject
    AgreementsBean agreementsBean;
    @Inject
    InvoiceAuthorization authorization;
    @Inject
    InvoiceBean invoiceBean;
    @Inject
    SMTPMailProducer mailProducer;
    @Inject
    private InvoiceReporter invoiceReporter;

    private Properties issuerProperties;
    private Issuer issuer;

    @PostConstruct
    public void setupIssuer() {
        setIssuer(issuerBean.getIssuerById(1L));
        setIssuerProperties(agreementsBean.readSmtpConfig());
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    @Override
    public void onMessage(final Message queueMessage) {
        final QueryAuthorizationMessage queryAuthorizationMessage = extractAuthorizationMessage(queueMessage);

        String accessKey = queryAuthorizationMessage.getAccessKey();
        final List<Autorizacion> authResponse = authorization.query(accessKey, ServiceEnvironment.TEST);

        if (authResponse.isEmpty()) {
            throw new NotAnswerException(accessKey, ServiceEnvironment.TEST);
        }

        final Autorizacion autorizacion = authResponse.get(0);
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

            saveAuthorizationDocument(queryAuthorizationMessage, autorizacion);
            recordResult(queryAuthorizationMessage, InvoicingStatus.AUTORIZED, accessKey,
                    messagesInTheResponse.toString());

            List<Attachment> attachments;
            try {
                attachments = generateAttachments(autorizacion, authNumber);
                sendNotificationToEmail(queryAuthorizationMessage, autorizacion, authNumber, attachments);
            } catch (InvoicePrintException e) {
                recordResult(queryAuthorizationMessage, InvoicingStatus.FAILED_TO_GENERATE_ATTACHEMETS, accessKey,
                        e.getMessage());
                LOG.error("access key = " + accessKey, e);
            }
        } else {
            recordResult(queryAuthorizationMessage, InvoicingStatus.NOT_AUTORIZED, accessKey,
                    messagesInTheResponse.toString());
        }
    }

    private QueryAuthorizationMessage extractAuthorizationMessage(final Message queueMessage) {
        final ObjectMessage objectMessage;
        try {
            objectMessage = ObjectMessage.class.cast(queueMessage);
        } catch (final ClassCastException cce) {
            throw new RuntimeException("Incorrect message type sent to object message consumer; got:"
                    + queueMessage.getClass().getSimpleName(), cce);
        }
        final QueryAuthorizationMessage queryAuthorizationMessage;
        try {
            final Object obj = objectMessage.getObject();
            queryAuthorizationMessage = QueryAuthorizationMessage.class.cast(obj);
        } catch (final JMSException jmse) {
            throw new RuntimeException("Could not unwrap JMS Message", jmse);
        } catch (final ClassCastException cce) {
            throw new RuntimeException("Expected message contents of type " + InvoiceLine.class.getName(), cce);
        }
        return queryAuthorizationMessage;
    }

    private List<Attachment> generateAttachments(final Autorizacion autorizacion, String authNumber)
            throws InvoicePrintException {
        List<Attachment> attachments = new ArrayList<Attachment>();
        attachments.add(new Attachment(autorizacion.getComprobante().getBytes(), authNumber + ".xml", "text/xml"));
        attachments.add(new Attachment(invoiceBean.printInvoice(autorizacion, autorizacion.getComprobante(), issuer),
                authNumber + ".pdf", "application/pdf"));
        return attachments;
    }

    private void sendNotificationToEmail(final QueryAuthorizationMessage queryAuthorizationMessage,
                                         final Autorizacion autorizacion, String authNumber, List<Attachment> attachments) {
        final String accessKey = queryAuthorizationMessage.getAccessKey();
        try {
            final String email = queryAuthorizationMessage.getInvoiceSubmission().getInvoice().getEmail().split(",")[0];
            final String emailBody = "Estimado Cliente,\nEsta factura digital es s\u00f3lo una copia de su factura f\u00edsica, la misma que de momento no tiene validez tributaria por encontrarse en m\u00f3dulo de pruebas. Los servicios y valores detallados deben coincidir con su factura f\u00edsica, de no ser as\u00ed; le solicitamos que nos lo comunique a: sacfyc@trans-telco.com. Tome en cuenta que los valores reales a cancelar ser\u00e1n los de la factura f\u00edsica. Si la direcci\u00f3n de correo en la cual ha recibido su factura no es encuentra correcta, le solicitamos que nos haga conocer para actualizar sus datos.\nMuchas Gracias por su comprensi\u00f3n.\nN\u00famero de Autorizaci\u00f3n: %s Fecha Autorizaci\u00f3n: %s";
            final MailMessage multiPartMessage = new MailMessageBuilder()
                    .from(issuer.getContactMail())
                    .addTo(email)
                    .subject("Facturacion Electronica Transtelco")
                    .body(String.format(emailBody, authNumber,
                            autorizacion.getFechaAutorizacion())).contentType("text/plain").hasAttachment(Boolean.TRUE)
                    .attachments(attachments).build();
            mailProducer.queueMailForDelivery(multiPartMessage);
        } catch (IllegalStateException | IllegalArgumentException e) {
            recordResult(queryAuthorizationMessage, InvoicingStatus.FAILED_TO_SEND_EMAIL, accessKey, e.getMessage());
            LOG.error(e);
        }
    }

    private void saveAuthorizationDocument(final QueryAuthorizationMessage queryAuthorizationMessage,
                                           final Autorizacion autorizacion) {
        Document document = new Document();
        document.setAuthorizationNumber(autorizacion.getNumeroAutorizacion());
        document.setAccessKey(queryAuthorizationMessage.getAccessKey());
        document.setIssuer(getIssuer());
        document.setEmisionDate(new Date());
        document.setDocumentType(DocumentType.FACTURA);
        document.setSignedXml(autorizacion.getComprobante().getBytes());
        document.setState(AuthorizationState.AUTORIZADO);
        invoiceBean.store(document);
    }

    private void recordResult(final QueryAuthorizationMessage authMessage, InvoicingStatus status, String accessKey,
                              String details) {
        invoiceReporter.recordEntryFor(authMessage.getInvoiceSubmission(), status, accessKey, details);
    }

    /**
     * @return the issuerProperties
     */
    public Properties getIssuerProperties() {
        return issuerProperties;
    }

    /**
     * @param issuerProperties the issuerProperties to set
     */
    public void setIssuerProperties(Properties issuerProperties) {
        this.issuerProperties = issuerProperties;
    }

    /**
     * @return the issuer
     */
    public Issuer getIssuer() {
        return issuer;
    }

    /**
     * @param issuer the issuer to set
     */
    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
    }

}
