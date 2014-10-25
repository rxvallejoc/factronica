/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.obiectumclaro.factronica.core.enumeration.Environment;
import com.obiectumclaro.factronica.core.enumeration.IdType;
import com.obiectumclaro.factronica.core.enumeration.IssuingMode;
import com.obiectumclaro.factronica.core.model.Document;
import com.obiectumclaro.factronica.core.model.Invoice;
import com.obiectumclaro.factronica.core.model.InvoiceItem;
import com.obiectumclaro.factronica.core.model.Issuer;
import com.obiectumclaro.factronica.core.model.TaxValue;
import com.obiectumclaro.factronica.core.model.access.DocumentEaoBean;
import com.obiectumclaro.factronica.core.model.access.InvoiceEaoBean;
import com.obiectumclaro.factronica.core.model.access.TaxValueEaoBean;
import com.obiectumclaro.factronica.core.model.xsd.invoice.SignedInvoice;
import com.obiectumclaro.factronica.core.model.xsd.invoice.SignedInvoice.InfoFactura.TotalConImpuestos.TotalImpuesto;
import com.obiectumclaro.factronica.core.service.exception.InvoicePrintException;
import com.obiectumclaro.factronica.core.service.exception.InvoiceSavingException;
import com.obiectumclaro.factronica.core.service.exception.InvoiceServiceException;
import com.obiectumclaro.factronica.core.web.service.sri.client.authorization.Autorizacion;
import com.obiectumclaro.templates.exception.TemplateGenerationException;
import com.obiectumclaro.templates.generator.TemplateGenerator;
import com.obiectumclaro.templates.model.FileFormat;
import com.obiectumclaro.templates.model.SimpleTemplate;
import com.obiectumclaro.templates.model.TemplateType;

/**
 * 
 * @author marco zaragocin
 */
@Stateless
public class InvoiceBean {
	
	private static final Logger LOG=Logger.getLogger(InvoiceBean.class);

    @EJB
    private DocumentEaoBean documentEaoBean;
    @EJB
    private InvoiceEaoBean invoiceEaoBean;
    @EJB
    private JaxbMarshallerBean marshallerBean;
    @EJB
    private TaxValueEaoBean taxValueEaoBean;

    public void store(Document document) {
        documentEaoBean.store(document);
    }

    public void save(final Invoice invoice, String signedInvoiceXml, Autorizacion autorizacion, Long customerId)
        throws InvoiceSavingException {
        try {
            String authorizationXml = new String(marshallerBean.marshall(autorizacion));
            invoice.setAuthorizationXml(authorizationXml);
            invoice.setXml(signedInvoiceXml);
            invoice.setCustomerId(customerId);
            invoiceEaoBean.save(invoice);
            send(invoice.getPk());
        } catch (JAXBException e) {
            throw new InvoiceSavingException(String.format("No se puede convertir la autorizacion a xml, Error: %s",
                e.getMessage()), e);
        } catch (MessagingException e) {
            Log.error("No se pudo enviar el mail con la factura", e);
        }catch (IOException e) {
            Log.error("No se pudo enviar el mail con la factura", e);
        }
        
    }

    public void send(Long invoiceId) throws MessagingException {
        Invoice invoice = invoiceEaoBean.findByPk(invoiceId);
        final Properties smtpConfig = readSmtpConfig();
        final Session session = startSessionFor(smtpConfig);
        Message message;
        try {
            TemplateGenerator templateGenerator = new TemplateGenerator(new SimpleTemplate(
                getBytesFromFile("sendInvoice.vm"), TemplateType.VELOCITY));
            String textMessage = createCustomerMailMessage(invoice, templateGenerator);

            message = composeNewMessage(session, invoice.getCustomer().getEmail(), textMessage);
            Transport.send(message);

        } catch (IOException e) {
        	LOG.error(e);
        }

    }

    public Properties readSmtpConfig() {
        try {
            final Properties smtpConfig = new Properties();
            final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
            final InputStream file = ccl.getResourceAsStream("factronica.properties");
            smtpConfig.load(file);
            return smtpConfig;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read configuration file factronica.properties", e);
        }
    }

    public List<Invoice> FindAll() {
        return invoiceEaoBean.findAll();
    }

    public byte[] printInvoice(Long invoiceId,Issuer issuer) throws InvoicePrintException {

        Invoice invoice = invoiceEaoBean.findByPk(invoiceId);
        if (invoice == null) {
            throw new InvoicePrintException(String.format("No se encuentra la factura con id: %s", invoiceId));
        }

        if (invoice.getAuthorizationXml() == null) {
            throw new InvoicePrintException(String.format("No existe una autorizacion para la factura %s", invoiceId));
        }
        return printInvoice(invoice.getAuthorizationXml(), invoice.getXml(),issuer);

    }

    // TODO este metodo debe ser privado, se debe quedar solo hasta que se
    // guarde la factura
    public byte[] printInvoice(Autorizacion authorization, String signedInvoiceXml,Issuer issuer) throws InvoicePrintException {
        try {
            TemplateGenerator templateGenerator = new TemplateGenerator(new SimpleTemplate("invoice.jasper",
                TemplateType.JASPER));
            SignedInvoice i = marshallerBean.unmarshall(SignedInvoice.class, signedInvoiceXml.getBytes());
            Map<String, Object> parameters = createInvoiceParameterMap(authorization, i,issuer);
            return templateGenerator.generate(parameters, i.getDetalles().getDetalle(), FileFormat.PDF);
        } catch (TemplateGenerationException e) {
            throw new InvoicePrintException(e);
        } catch (JAXBException e) {
            throw new InvoicePrintException(e);
        }
    }

    public void generateInvoice(Invoice invoice) throws InvoiceServiceException {
        hasCustomer(invoice);
        validateInvoiceProducts(invoice);
    }

    private Session startSessionFor(final Properties smtpConfig) {
        return Session.getInstance(smtpConfig, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("obiectumclaro@gmail.com", "ob12345678");
            }
        });
    }

    private String createCustomerMailMessage(Invoice invoice, TemplateGenerator templateGenerator) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("customer", invoice.getCustomer());
        parameters.put("invoiceNumber", invoice.getPk());
        parameters.put("invoiceId", invoice.getPk());
        try {
            return new String(templateGenerator.generate(parameters));
        } catch (TemplateGenerationException e) {
            return "";
        }
    }
    
    private byte[] getBytesFromFile(String fileName) throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        int size = inputStream.available();
        byte[] bytes = new byte[size];
        inputStream.read(bytes);
        return bytes;
    }

    
    private Message composeNewMessage(final Session session, String destinatary, String textMessage)
            throws MessagingException {
            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("notificaciones@trans-telco.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatary));
            message.setSubject("Factura");
            message.setText(textMessage);
            return message;
    }

    private byte[] printInvoice(String authorizationXml, String signedInvoiceXml,Issuer issuer) throws InvoicePrintException {
        try {
            Autorizacion authorization = marshallerBean.unmarshall(Autorizacion.class, authorizationXml.getBytes());
            return printInvoice(authorization, signedInvoiceXml,issuer);
        } catch (JAXBException e) {
            throw new InvoicePrintException(e);
        }
    }

    private Map<String, Object> createInvoiceParameterMap(Autorizacion authorization, SignedInvoice signedInvoice,Issuer issuer) {

        Map<String, Object> parametersMap= new HashMap<>();
        final int ambiente = Integer.parseInt(signedInvoice.getInfoTributaria().getAmbiente());
        final int tipoEmision = Integer.parseInt(signedInvoice.getInfoTributaria().getTipoEmision());
        final String tipoId = signedInvoice.getInfoFactura().getTipoIdentificacionComprador();
        final String guiaRemision = signedInvoice.getInfoFactura().getGuiaRemision();
        SignedInvoice.InfoAdicional infoAdicional = signedInvoice.getInfoAdicional();
        try {
			parametersMap.put("logo", ImageIO.read(new ByteArrayInputStream(issuer.getLogo())));
		} catch (IOException e) {
			LOG.error("Error al Cargar el Logo",e);
		}
        
        parametersMap.put("ruc", signedInvoice.getInfoTributaria().getRuc());
        parametersMap.put("numeroFactura", signedInvoice.getInfoTributaria().getSecuencial());
        parametersMap.put("numeroAutorizacion", authorization.getNumeroAutorizacion());
        parametersMap.put("fechaAutorizacion",new SimpleDateFormat("dd - MM - yyyy hh:mm:ss").format(authorization.getFechaAutorizacion().toGregorianCalendar().getTime()));
        parametersMap.put("ambiente", Environment.valueOf(ambiente).name());        
        parametersMap.put("tipoEmision", IssuingMode.valueOf(tipoEmision).name());
        parametersMap.put("claveAcceso", signedInvoice.getInfoTributaria().getClaveAcceso());
        parametersMap.put("nombreEmisor", signedInvoice.getInfoTributaria().getNombreComercial());
        parametersMap.put("direccionMatriz", signedInvoice.getInfoTributaria().getDirMatriz());
        parametersMap.put("direccionSucursal", signedInvoice.getInfoFactura().getDirEstablecimiento());
        parametersMap.put("contribuyente", signedInvoice.getInfoTributaria().getRazonSocial());
        parametersMap.put("obligadoContabilidad", signedInvoice.getInfoFactura().getObligadoContabilidad());
        parametersMap.put("razonSocialCliente", signedInvoice.getInfoFactura().getRazonSocialComprador());
        parametersMap.put("idCliente", signedInvoice.getInfoFactura().getIdentificacionComprador());
        parametersMap.put("tipoIdCliente", IdType.getTypeOf(tipoId).name());
        parametersMap.put("fechaEmision", signedInvoice.getInfoFactura().getFechaEmision());
        parametersMap.put("guiaRemision", guiaRemision != null ? guiaRemision : "");
        parametersMap.put("impuestos", getTaxes(signedInvoice.getInfoFactura().getTotalConImpuestos().getTotalImpuesto()));
        parametersMap.put("subtotalNoImpuesto", signedInvoice.getInfoFactura().getTotalSinImpuestos() == null ? "0.00" : signedInvoice.getInfoFactura().getTotalSinImpuestos().toPlainString());
        parametersMap.put("valorDescuento", signedInvoice.getInfoFactura().getTotalDescuento() == null ? "0.00" : signedInvoice.getInfoFactura().getTotalDescuento().toPlainString());
        parametersMap.put("valorPropina", signedInvoice.getInfoFactura().getPropina() == null ? "0.00" : signedInvoice.getInfoFactura().getPropina().toPlainString());
        parametersMap.put("valorTotal", signedInvoice.getInfoFactura().getImporteTotal() == null ? "0.00" : signedInvoice.getInfoFactura().getImporteTotal().toPlainString());
        if (infoAdicional != null) {
        	parametersMap.put("infoAdicional", infoAdicional.getCampoAdicional());
        }
        return parametersMap;

    }
    
    private List<TaxValue> getTaxes(List<TotalImpuesto> totalImpuestos) {
        List<TaxValue> result = new ArrayList<>();
        TaxValue taxValue;
        for (TotalImpuesto ti : totalImpuestos) {
            taxValue = taxValueEaoBean.findByCode(ti.getCodigo());
            taxValue.setValue(ti.getValor());
            result.add(taxValue);
        }
        return result;
    }

    private void hasCustomer(Invoice invoice) throws InvoiceServiceException {
        if (invoice.getCustomer().getId() == null || invoice.getCustomer().getId().isEmpty()) {
            throw new InvoiceServiceException("Debe registrar los datos del consumidor");
        }
    }

    private void validateInvoiceProducts(Invoice invoice) throws InvoiceServiceException {
        if (invoice.getItems() == null && invoice.getItems().isEmpty()) {
            throw new InvoiceServiceException("La factura no tiene productoos");
        }
        for (InvoiceItem item : invoice.getItems()) {
            if (item == null) {
                throw new InvoiceServiceException("Debe registrar los datos del consumidor");
            }

            if (item.getCode() == null || item.getAmount().equals("0")) {
                throw new InvoiceServiceException("El codigo de producto es obligatorioo");
            }
            if (item.getAmount() == null || item.getAmount().equals("0")) {
                throw new InvoiceServiceException("La cantidad de productos es Obligatorio");
            }
        }
    }

}
