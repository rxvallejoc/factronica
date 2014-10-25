/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.pos.backing.nota.credito;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.obiectumclaro.factronica.core.enumeration.IdType;
import com.obiectumclaro.factronica.core.enumeration.Person;
import com.obiectumclaro.factronica.core.model.Customer;
import com.obiectumclaro.factronica.core.model.Issuer;
import com.obiectumclaro.factronica.core.model.Product;
import com.obiectumclaro.factronica.core.model.TaxValue;
import com.obiectumclaro.factronica.core.model.access.CustomerEaoBean;
import com.obiectumclaro.factronica.core.model.access.ProductEaoBean;
import com.obiectumclaro.factronica.core.model.xsd.nota.credito.Impuesto;
import com.obiectumclaro.factronica.core.model.xsd.nota.credito.InfoTributaria;
import com.obiectumclaro.factronica.core.model.xsd.nota.credito.NotaCredito;
import com.obiectumclaro.factronica.core.model.xsd.nota.credito.TotalConImpuestos;
import com.obiectumclaro.factronica.core.service.IssuerBean;
import com.obiectumclaro.factronica.core.service.NotaCreditoBean;
import com.obiectumclaro.factronica.api.invoice.exception.SignerException;
import com.obiectumclaro.factronica.core.sign.SignerBean;
import com.obiectumclaro.factronica.core.web.service.sri.client.InvoiceAuthorization;
import com.obiectumclaro.factronica.core.web.service.sri.client.ReturnedInvoiceException;
import com.obiectumclaro.factronica.core.web.service.sri.client.ServiceEnvironment;
import com.obiectumclaro.factronica.core.web.service.sri.client.authorization.Autorizacion;
import com.obiectumclaro.factronica.core.web.service.sri.client.reception.Comprobante;
import com.obiectumclaro.factronica.pos.jsf.FacesMessageHelper;
import com.obiectumclaro.factronica.web.constant.Constant;

/**
 * 
 * @author marco
 */
@ViewScoped
@ManagedBean
public class NotaCreditoController {

    @EJB
    private SignerBean signer;
    @EJB
    ProductEaoBean productBean;
    @EJB
    private CustomerEaoBean customerEaoBean;
    @EJB
    private IssuerBean issuerBean;
    @EJB
    private NotaCreditoBean notaCreditoService;
    @EJB
    private InvoiceAuthorization authorization;
    private Autorizacion authInvoice;
    private Issuer issuer;
    private Customer customer;
    private Customer selectedCustomer;
    private NotaCredito notaCredito;
    private List<NotaCredito.Detalles.Detalle> detallesNotaCredito;
    private String codigoProducto;
    private Product selectedProduct;

    public NotaCreditoController() {
        this.customer = new Customer();
        this.detallesNotaCredito = new ArrayList<NotaCredito.Detalles.Detalle>();
        newNotaCredito();
        getNotaCredito().setDetallesNotaCredito(detallesNotaCredito);
    }

    @PostConstruct
    public void initData() {
        try {
            Issuer i = issuerBean.getIssuerById(1L);
            setIssuer(i);
        } catch (Exception e) {
            FacesMessageHelper.addError("No existe configurado el Emisor", "No existe configurado el Emisor");
        }

    }

    /***
     * 
     * 
     * 
     * 
     * */

    public void addDetalle() {
        NotaCredito.Detalles.Detalle detalle = addProduct();
        Product p = productBean.findByCode(getSelectedProduct().getCode());
        if (p != null) {
            detalle.setCodigoInterno(p.getCode());
            detalle.setDescripcion(p.getDescription());
            detalle.setPrecioUnitario(p.getUnitPrice());
            detalle.setPrecioTotalSinImpuesto(BigDecimal.ZERO);
            detalle.setDescuento(p.getDiscount());
            detalle.setProduct(p);
            calcularTotal(detalle);
        } else {
            FacesMessageHelper.addError("No existe producto con el codigo " + detalle.getCodigoInterno(),
                "No existe producto con el codigo " + detalle.getCodigoInterno());
        }
        getDetallesNotaCredito().add(detalle);
    }

    public void searchProductByCode(NotaCredito.Detalles.Detalle detalle) {
        if (detalle.getCodigoInterno() != null && !detalle.getCodigoInterno().isEmpty()) {
            Product p = productBean.findByCode(detalle.getCodigoInterno());
            if (p != null) {
                detalle.setDescripcion(p.getDescription());
                detalle.setPrecioUnitario(p.getUnitPrice());
                detalle.setPrecioTotalSinImpuesto(BigDecimal.ZERO);
                detalle.setDescuento(p.getDiscount());
                detalle.setProduct(p);
                calcularTotal(detalle);
            } else {
                FacesMessageHelper.addError("No existe producto con el codigo " + detalle.getCodigoInterno(),
                    "No existe producto con el codigo " + detalle.getCodigoInterno());
            }

        }
    }

    public void deletedetalle(NotaCredito.Detalles.Detalle detalle) {
        getDetallesNotaCredito().remove(detalle);
    }

    private void newNotaCredito() {
        setNotaCredito(new NotaCredito());
        getNotaCredito().setInfoNotaCredito(new NotaCredito.InfoNotaCredito());
        getNotaCredito().setInfoTributaria(new InfoTributaria());
        List<NotaCredito.Detalles> detallesList = new ArrayList<>();
        detallesList.add(new NotaCredito.Detalles());
        // detalles.get(0).getDetalle().get(0).
        // setDetalles(detallesList);
        // getNotaCredito().getDetalles().getDetalle().add(detalle);
        // setNotaCredito(notaCredito);
    }

    private String obtenerTipoIdentificacionComprador(IdType idType) {
        if (IdType.RUC.name().equals(idType.name())) {
            return "04";
        }
        if (IdType.CEDULA.name().equals(idType.name())) {
            return "05";
        }
        if (IdType.PASAPORTE.name().equals(idType.name())) {
            return "04";
        }
        return "07";
    }

    private void setDataNotaCredito() {
        getNotaCredito().setId("comprobante");
        getNotaCredito().setVersion("1.1.0");
        getNotaCredito().getInfoTributaria().setCodDoc("04");
        getNotaCredito().getInfoTributaria().setRazonSocial(issuer.getSocialReason());

        getNotaCredito().getInfoTributaria().setDirMatriz(issuer.getAddres());
        getNotaCredito().getInfoTributaria().setRuc(issuer.getRuc());
        getNotaCredito().getInfoTributaria().setAmbiente(issuer.getEnvironment().getDescripcion());
        getNotaCredito().getInfoNotaCredito().setTipoIdentificacionComprador(
            obtenerTipoIdentificacionComprador(customer.getIdType()));
        getNotaCredito().getInfoNotaCredito().setIdentificacionComprador(customer.getId());
        if (Person.JURIDICA.equals(customer.getPerson())) {
            getNotaCredito().getInfoNotaCredito().setRazonSocialComprador(customer.getSocialReason());
        } else {
            getNotaCredito().getInfoNotaCredito().setRazonSocialComprador(customer.getName());
        }
        getNotaCredito().getInfoNotaCredito().setTotalSinImpuestos(getNotaCredito().getTotalExcludedTaxes());
        getNotaCredito().getInfoNotaCredito().setValorModificacion(getNotaCredito().getTotalDue());
    }

    /**
     * <codigoInterno>codigoInterno0</codigoInterno> <descripcion>descripcion0</descripcion> <cantidad>50.00</cantidad>
     * <precioUnitario>50.00</precioUnitario> <precioTotalSinImpuesto>50.00</precioTotalSinImpuesto>
     * 
     * 
     */
    private void setDetallesNotaCReditoXML() {
        NotaCredito.Detalles detalles = new NotaCredito.Detalles();
        for (NotaCredito.Detalles.Detalle detalle : getNotaCredito().getDetallesNotaCredito()) {
            detalles.getDetalle().add(detalle);

            NotaCredito.Detalles.Detalle.Impuestos impuestos = new NotaCredito.Detalles.Detalle.Impuestos();
            for (TaxValue taxValue : detalle.getProduct().getTaxValueList()) {
                Impuesto tax = new Impuesto();
                tax.setBaseImponible(detalle.getPrecioTotalSinImpuesto());
                tax.setCodigo(taxValue.getTaxId().getCode().toString());
                tax.setCodigoPorcentaje(taxValue.getTaxValueCode());
                tax.setValor(detalle.getPrecioUnitario().multiply(taxValue.getRate().divide(new BigDecimal(100)))
                    .multiply(detalle.getCantidad()).setScale(2, BigDecimal.ROUND_UP));
                tax.setTarifa(taxValue.getRate());
                impuestos.getImpuesto().add(tax);
            }
            detalle.setPrecioTotalSinImpuesto(detalle.getProduct().getUnitPrice().multiply(detalle.getCantidad())
                .setScale(2, BigDecimal.ROUND_UP));

            detalle.setImpuestos(impuestos);
        }
        getNotaCredito().setDetalles(detalles);
    }

    private void setInfoNotaCreditoTotalImpuestos() {
        // NotaCredito.Detalles detalles = new NotaCredito.Detalles();
        TotalConImpuestos totalconImpuestos = new TotalConImpuestos();
        for (NotaCredito.Detalles.Detalle detalle : getNotaCredito().getDetallesNotaCredito()) {
            // totalconImpuestos.getTotalImpuesto()getDetalle().add(detalle);

            // NotaCredito.Detalles.Detalle.Impuestos impuestos= new NotaCredito.Detalles.Detalle.Impuestos();
            for (TaxValue taxValue : detalle.getProduct().getTaxValueList()) {
                // Impuesto tax= new Impuesto();
                TotalConImpuestos.TotalImpuesto tax = new TotalConImpuestos.TotalImpuesto();
                tax.setBaseImponible(detalle.getPrecioTotalSinImpuesto());
                tax.setCodigo(taxValue.getTaxId().getCode().toString());
                tax.setCodigoPorcentaje(taxValue.getTaxValueCode());
                tax.setValor(detalle.getPrecioUnitario().multiply(taxValue.getRate().divide(new BigDecimal(100)))
                    .multiply(detalle.getCantidad()).setScale(2, BigDecimal.ROUND_UP));
                // tax.setTarifa(taxValue.getRate());
                totalconImpuestos.getTotalImpuesto().add(tax);
            }
            // detalle.setPrecioTotalSinImpuesto(detalle.getProduct().getUnitPrice().multiply(detalle.getCantidad()).setScale(2,BigDecimal.ROUND_UP));

            // detalle.setImpuestos(impuestos);
        }
        getNotaCredito().getInfoNotaCredito().setTotalConImpuestos(totalconImpuestos);
    }

    /**
     * ****
     * 
     * 
     * 
     * 
     */
    public void generarNotaDeCredito() {

        if (getCustomer() != null && customer.getId() != null) {
            if (getNotaCredito().getDetallesNotaCredito().isEmpty()) {
                FacesMessageHelper.addError("Debe seleccionar al menos un producto",
                    "Debe seleccionar al menos un producto");
            } else {
                setDataNotaCredito();
                setDetallesNotaCReditoXML();
                setInfoNotaCreditoTotalImpuestos();
                setNotaCredito(notaCreditoService.generarNotaCredito(notaCredito));
                byte[] file;
                try {
                    file = signer.sign(fileToByteArray(getNotaCredito().getFile()),issuer.getCertificate(),issuer.getPassword());
                    System.out.println("-------> XML  " + fileToByteArray(getNotaCredito().getFile()));
                    final List<Autorizacion> authResponse = authorization.syncRequest(file, ServiceEnvironment.TEST, 5);
                    final Autorizacion autorizacion = authResponse.get(0);
                    FacesMessageHelper.addInfo(String.format("Nota de Crédito %s", autorizacion.getEstado()), null);
                    FacesMessageHelper.addQueryMessages(autorizacion.getMensajes().getMensaje());
                    setAuthInvoice(autorizacion.getNumeroAutorizacion() == null ? null : autorizacion);
                } catch (ReturnedInvoiceException rie) {
                    final Comprobante comprobante = authorization.getComprobantes().get(0);
                    FacesMessageHelper.addRequestMessages(comprobante.getMensajes().getMensaje());
                } catch (SignerException e) {
                    FacesMessageHelper.addError("Hubo un error al firmar el documento", e.getMessage());
                }
            }
        } else {
            FacesMessageHelper.addError("Debe seleccionar cliente", "Debe seleccionar cliente");
        }
    }

    public byte[] fileToByteArray(File archivo) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(archivo);
            byte[] contenido = new byte[(int) archivo.length()];
            inputStream.read(contenido);
            return contenido;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void calcularTotal(NotaCredito.Detalles.Detalle detalle) {
        System.out.println("DESCUENTO----->" + detalle.getDescuento());
        if (detalle.getDescuento() != null) {
            detalle.setPrecioTotalSinImpuesto((detalle.getCantidad().multiply(detalle.getPrecioUnitario()))
                .subtract(detalle.getDescuento()));
        } else {
            detalle.setPrecioTotalSinImpuesto((detalle.getCantidad().multiply(detalle.getPrecioUnitario())));
        }
    }

    public NotaCredito.Detalles.Detalle addProduct() {
        NotaCredito.Detalles.Detalle det = new NotaCredito.Detalles.Detalle();
        det.setCantidad(BigDecimal.ONE);
        det.setPrecioUnitario(BigDecimal.ZERO);
        det.setPrecioTotalSinImpuesto(BigDecimal.ZERO);
        det.setDescuento(BigDecimal.ZERO);
        // getDetallesNotaCredito().add(det);
        return det;

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

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer
     *            the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the notaCredito
     */
    public NotaCredito getNotaCredito() {
        return notaCredito;
    }

    /**
     * @param notaCredito
     *            the notaCredito to set
     */
    public void setNotaCredito(NotaCredito notaCredito) {
        this.notaCredito = notaCredito;
    }

    /**
     * @return the selectedCustomer
     */
    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    /**
     * @param selectedCustomer
     *            the selectedCustomer to set
     */
    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    /**
     * 
     * @param query
     * @return
     */
    public List<Customer> complete(String query) {
        return customerEaoBean.findByName(query);

    }

    public void searchCustomer(String id) {
        if (Constant.SEARCH_CUSTOMMER.equals(id)) {
            findCustomer();
        }

    }

    public void findCustomer() {
        setCustomer(selectedCustomer);
        if (getCustomer() == null) {
            FacesMessageHelper.addFacesMessage(FacesMessage.SEVERITY_INFO,
                "No existe usuario con este numero de identificacion ", null);
        }
    }

    public List<Customer> queryCustomer(String query) {
        return customerEaoBean.findByName(query);

    }

    public List<Product> queryProduct(String query) {
        return productBean.findProducts(query);

    }

    /**
     * @return the detalles
     */

    public List<NotaCredito.Detalles.Detalle> getDetallesNotaCredito() {
        return detallesNotaCredito;
    }

    /**
     * @param detalles
     *            the detalles to set
     */
    public void setDetallesNotaCredito(List<NotaCredito.Detalles.Detalle> detalles) {
        this.detallesNotaCredito = detalles;
    }

    /**
     * @return the codigoProducto
     */
    public String getCodigoProducto() {
        return codigoProducto;
    }

    /**
     * @param codigoProducto
     *            the codigoProducto to set
     */
    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    /**
     * @return the authInvoice
     */
    public Autorizacion getAuthInvoice() {
        return authInvoice;
    }

    /**
     * @param authInvoice
     *            the authInvoice to set
     */
    public void setAuthInvoice(Autorizacion authInvoice) {
        this.authInvoice = authInvoice;
    }

    /**
     * @return the selectedProduct
     */
    public Product getSelectedProduct() {
        return selectedProduct;
    }

    /**
     * @param selectedProduct
     *            the selectedProduct to set
     */
    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }
}
