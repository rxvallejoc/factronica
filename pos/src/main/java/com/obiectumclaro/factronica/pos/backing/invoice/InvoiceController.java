/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.pos.backing.invoice;

import com.obiectumclaro.factronica.core.enumeration.DocumentType;
import com.obiectumclaro.factronica.core.mail.MailMessageBuilder;
import com.obiectumclaro.factronica.core.mail.SMTPMailProducer;
import com.obiectumclaro.factronica.core.mail.SMTPMailService;
import com.obiectumclaro.factronica.core.mail.model.Attachment;
import com.obiectumclaro.factronica.core.mail.model.MailMessage;
import com.obiectumclaro.factronica.core.model.*;
import com.obiectumclaro.factronica.core.model.access.CustomerEaoBean;
import com.obiectumclaro.factronica.core.model.access.ProductEaoBean;
import com.obiectumclaro.factronica.core.service.InvoiceBean;
import com.obiectumclaro.factronica.core.service.InvoiceFactoryBean;
import com.obiectumclaro.factronica.core.service.IssuerBean;
import com.obiectumclaro.factronica.core.service.exception.InvoicePrintException;
import com.obiectumclaro.factronica.api.invoice.exception.SignerException;
import com.obiectumclaro.factronica.core.sign.SignerBean;
import com.obiectumclaro.factronica.core.web.service.sri.client.AuthorizationState;
import com.obiectumclaro.factronica.core.web.service.sri.client.InvoiceAuthorization;
import com.obiectumclaro.factronica.core.web.service.sri.client.ReturnedInvoiceException;
import com.obiectumclaro.factronica.core.web.service.sri.client.ServiceEnvironment;
import com.obiectumclaro.factronica.core.web.service.sri.client.authorization.Autorizacion;
import com.obiectumclaro.factronica.core.web.service.sri.client.reception.Comprobante;
import com.obiectumclaro.factronica.pos.backing.BaseBackingBean;
import com.obiectumclaro.factronica.pos.jsf.FacesMessageHelper;
import com.obiectumclaro.factronica.pos.jsf.data.model.ProductDataModel;
import com.obiectumclaro.factronica.web.constant.Constant;
import org.apache.log4j.Logger;
import org.primefaces.component.api.UIData;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author marco zaragocin
 */
@ViewScoped
@ManagedBean
public class InvoiceController extends BaseBackingBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(InvoiceController.class);
    @EJB
    private ProductEaoBean productEaoBean;
    @EJB
    private CustomerEaoBean customerEaoBean;
    @EJB
    private SignerBean signer;
    @EJB
    private SMTPMailProducer mailProducer;
    @EJB
    private SMTPMailService mailService;
    @EJB
    private InvoiceAuthorization authorization;
    @EJB
    private InvoiceFactoryBean xmlGenerate;
    @EJB
    private IssuerBean issuerBean;
    @EJB
    private InvoiceBean invoiceBean;

    private String idCustomer;
    private Customer customer;
    private Product product;
    private Invoice invoice;
    private byte[] signedInvoice;
    private UIData productsTable;
    private Product selectedProduct;
    private Customer selectedCustomer;
    private ProductDataModel productDataModel;
    private Issuer issuer;
    private Autorizacion authInvoice;
    private List<Product> products;
    private Product deleteProduct;
    private List<InvoiceItem> invoiceItems;

    public InvoiceController() {
        this.customer = new Customer();
        this.invoice = new Invoice();
        this.invoiceItems = new ArrayList<InvoiceItem>();
        this.products = new ArrayList<Product>();
    }

    @PostConstruct
    public void initData() {
        try {
            this.issuer = issuerBean.getIssuerById(1L);
        } catch (Exception e) {
            FacesMessageHelper.addError("No existe configurado el Emisor", "No existe configurado el Emisor");
            LOG.error("No existe configurado el Emisor", e);
        }

    }

    public void addInformacionAdicional() {
        if (getInvoice().getAdditionalInfoList() != null && getInvoice().getAdditionalInfoList().size() < 16) {
            getInvoice().getAdditionalInfoList().add(new AdditionaInformationCustomer(null, null));
        } else {
            FacesMessageHelper.addError("Informacion AAdicional", "Informacion AAdicional");
        }

    }

    /**
     * Generacion del archivo XML y envio para su firma y autorizacion.
     *
     * @throws InvoicePrintException
     */
    public void generateInvoice() throws InvoicePrintException {
        authInvoice = null;
        try {
            signedInvoice = signer.sign(xmlGenerate.generateXMLFile(getInvoice(), getCustomer(), issuer), issuer.getCertificate(), issuer.getPassword());
            final List<Autorizacion> authResponse = authorization.syncRequest(signedInvoice, ServiceEnvironment.TEST, 20);
            if (!authResponse.isEmpty()) {
                final Autorizacion autorizacion = authResponse.get(0);
                final String authorizationStatus = autorizacion.getEstado();
                LOG.info(String.format("Invoice status: %s", authorizationStatus));
                FacesMessageHelper.addInfo(String.format("Factura %s", authorizationStatus), null);
                FacesMessageHelper.addQueryMessages(autorizacion.getMensajes().getMensaje());

                if (AuthorizationState.AUTORIZADO.name().equals(authorizationStatus)) {
                    authInvoice = autorizacion.getNumeroAutorizacion() == null ? null : autorizacion;
                    List<Attachment> attachments = createAttachments(autorizacion);
                    stroreDocument(AuthorizationState.AUTORIZADO);
                    sendMailWithAttachments(attachments);
                }
            } else {
                FacesMessageHelper.addError("Existe Problemas con el  Servicio de Rentas Internas ", "");
                stroreDocument(AuthorizationState.NO_AUTORIZADO);
            }

        } catch (ReturnedInvoiceException rie) {
            final Comprobante comprobante = rie.getComprobantes().get(0);
            FacesMessageHelper.addRequestMessages(comprobante.getMensajes().getMensaje());
            stroreDocument(AuthorizationState.NO_AUTORIZADO);
        } catch (SignerException e) {
            LOG.error(e, e);
            FacesMessageHelper.addError(String.format("Hubo un error al firmar el documento: %s", e.getMessage()), "");
            stroreDocument(AuthorizationState.NO_AUTORIZADO);
        } catch (Exception e) {
            FacesMessageHelper.addError("Existe Problemas con el  Servicio de Rentas Internas ", "Reintente la facturacion");
            stroreDocument(AuthorizationState.NO_AUTORIZADO);
        }
    }

    private List<Attachment> createAttachments(final Autorizacion autorizacion)
            throws InvoicePrintException {
        List<Attachment> attachments = new ArrayList<Attachment>();
        attachments.add(new Attachment(authInvoice.getComprobante().getBytes(), String.format("%s.xml",
                authInvoice.getNumeroAutorizacion()), "text/xml"));
        attachments.add(new Attachment(invoiceBean.printInvoice(autorizacion, authInvoice.getComprobante(), issuer), String
                .format("%s.pdf", authInvoice.getNumeroAutorizacion()), "application/pdf"));
        return attachments;
    }

    private void sendMailWithAttachments(List<Attachment> attachments) {
        LOG.info("Sending mail ........");
        final MailMessage multiPartMessage = new MailMessageBuilder()
                .from("notificaciones@trans-telco.com")
                .addTo(getCustomer().getEmail())
                .subject("Facturacion Electronica")
                .body(
                        "Estimado Consumidor, \nAdjunto enviamos su factura electrónica. \n¡Gracias por ser parte de nuestro compromiso con el medio ambiente!\n Trans Telco S.A. ")
                .contentType("text/plain").hasAttachment(Boolean.TRUE).attachments(attachments).build();
        mailProducer.queueMailForDelivery(multiPartMessage);
        LOG.info("Mail sended ........");
    }

    private void stroreDocument(AuthorizationState auth) {
        Document document = new Document();
        document.setAuthorizationNumber(authInvoice != null ? authInvoice.getNumeroAutorizacion() : "");
        document.setAccessKey(getInvoice().getAccesKey());
        document.setIssuer(getIssuer());
        document.setEmisionDate(new Date());
        document.setDocumentType(DocumentType.FACTURA);
        document.setSignedXml(signedInvoice);
        document.setState(auth);
        invoiceBean.store(document);
    }


    /**
     * Query for {@link Customer} search
     *
     * @param query
     * @return
     */
    public List<Customer> queryCustomer(String query) {
        return customerEaoBean.findByName(query);

    }

    /**
     * Query for {@link Product} search
     *
     * @param query
     * @return
     */
    public List<Product> queryProduct(String query) {
        return productEaoBean.findProducts(query);

    }

    /**
     * Search a {@link Customer} by id
     *
     * @param id
     */
    public void searchCustomer(String id) {
        if (Constant.SEARCH_CUSTOMMER.equals(id)) {
            findCustomer();
        }

    }

    public void findCustomer() {
        setCustomer(selectedCustomer);
        if (getCustomer() == null) {
            FacesMessageHelper.addFacesMessage(FacesMessage.SEVERITY_INFO,
                    "No existe usuario con este numero de identificacion " + getIdCustomer(), null);
        }
    }

    /**
     * Adds the {@link Product} to detail
     */
    public void addProduct() {
        if (products.contains(getSelectedProduct())) {
            FacesMessageHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR,
                    "El producto ya se encuentra agregado a la factura", null);
        } else {
            products.add(getSelectedProduct());
            invoice.addItem(new InvoiceItem(getSelectedProduct()));
        }
    }

    /**
     * Delete {@link Product} detail
     */
    public void deleteProduct() {
        products.remove(getDeleteProduct());
        invoice.getItems().remove(getProductsTable().getRowIndex());
    }


    public void totalProductoCaculation(final Product product) {
        if (product.getDiscount().compareTo(product.getTotal()) > 0) {
            FacesMessageHelper.addFacesMessage(FacesMessage.SEVERITY_INFO,
                    "El descuento no puede ser mayor que el precio unitario ", null);
        } else {
            products.get(getProductsTable().getRowIndex()).setTotal(
                    product.getAmount().multiply(product.getUnitPrice()).subtract(product.getDiscount()));

            invoice.getItems().get(getProductsTable().getRowIndex()).setAmount(product.getAmount());
            invoice.getItems().get(getProductsTable().getRowIndex()).setDiscount(product.getDiscount());
            invoice.getItems().get(getProductsTable().getRowIndex()).setTotal(
                    product.getAmount().multiply(product.getUnitPrice()).subtract(product.getDiscount()));
        }
    }


    public void printInvoice() {
        if (authInvoice != null) {
            try {
                final String signedInvoiceXml = new String(signedInvoice);
                byte[] ride = invoiceBean.printInvoice(authInvoice, signedInvoiceXml, this.issuer);
                downloadFile(ride, "application/pdf", "factura");
            } catch (InvoicePrintException e) {
                FacesMessageHelper.addError(
                        String.format("No se puede imprimir la factura: Error[%s]", e.getMessage()),
                        e.getLocalizedMessage());
            } catch (IOException e) {
                FacesMessageHelper.addError(
                        String.format("No se puede descargar la factura: Error[%s]", e.getMessage()),
                        e.getLocalizedMessage());
            }
        }
    }

    public byte[] fileToByteArray(File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            byte[] content = new byte[(int) file.length()];
            inputStream.read(content);
            return content;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }


    /**
     * @return the idCustomer
     */
    public String getIdCustomer() {
        return idCustomer;
    }

    /**
     * @param idCustomer the idCustomer to set
     */
    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {

        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the invoice
     */
    public Invoice getInvoice() {
        return invoice;
    }

    /**
     * @param invoice the invoice to set
     */
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    /**
     * @return the productsTable
     */
    public UIData getProductsTable() {
        return productsTable;
    }

    /**
     * @param productsTable the productsTable to set
     */
    public void setProductsTable(UIData productsTable) {
        this.productsTable = productsTable;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return the productDataModel
     */
    public ProductDataModel getProductDataModel() {
        return productDataModel;
    }

    /**
     * @param productDataModel the productDataModel to set
     */
    public void setProductDataModel(ProductDataModel productDataModel) {
        this.productDataModel = productDataModel;
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

    public Autorizacion getAuthInvoice() {
        return authInvoice;
    }

    /**
     * @return the selectedCustomer
     */
    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    /**
     * @param selectedCustomer the selectedCustomer to set
     */
    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    /**
     * @return the issuerBean
     */
    public IssuerBean getIssuerBean() {
        return issuerBean;
    }

    /**
     * @param issuerBean the issuerBean to set
     */
    public void setIssuerBean(IssuerBean issuerBean) {
        this.issuerBean = issuerBean;
    }

    /**
     * @return the productEao
     */
    public ProductEaoBean getProductEao() {
        return productEaoBean;
    }

    /**
     * @param productEao the productEao to set
     */
    public void setProductEao(ProductEaoBean productEao) {
        this.productEaoBean = productEao;
    }

    /**
     * @return the customerEaoBean
     */
    public CustomerEaoBean getCustomerEaoBean() {
        return customerEaoBean;
    }

    /**
     * @param customerEaoBean the customerEaoBean to set
     */
    public void setCustomerEaoBean(CustomerEaoBean customerEaoBean) {
        this.customerEaoBean = customerEaoBean;
    }

    /**
     * @return the selectedProduct
     */
    public Product getSelectedProduct() {
        return selectedProduct;
    }

    /**
     * @param selectedProduct the selectedProduct to set
     */
    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    /**
     * @return the products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * @param products the products to set
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * @return the deleteProduct
     */
    public Product getDeleteProduct() {
        return deleteProduct;
    }

    /**
     * @param deleteProduct the deleteProduct to set
     */
    public void setDeleteProduct(Product deleteProduct) {
        this.deleteProduct = deleteProduct;
    }

    /**
     * @return the invoiceItems
     */
    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    /**
     * @param invoiceItems the invoiceItems to set
     */
    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }


}