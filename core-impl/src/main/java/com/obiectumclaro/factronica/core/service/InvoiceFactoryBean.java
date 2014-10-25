/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;

import com.obiectumclaro.factronica.core.access.key.AccessKey;
import com.obiectumclaro.factronica.core.access.key.AccessKeyBuilder;
import com.obiectumclaro.factronica.core.enumeration.IdType;
import com.obiectumclaro.factronica.core.model.AdditionaInformationCustomer;
import com.obiectumclaro.factronica.core.model.Customer;
import com.obiectumclaro.factronica.core.model.Invoice;
import com.obiectumclaro.factronica.core.model.InvoiceItem;
import com.obiectumclaro.factronica.core.model.Issuer;
import com.obiectumclaro.factronica.core.model.TaxValue;
import com.obiectumclaro.factronica.core.model.TotalizedTax;
import com.obiectumclaro.factronica.core.model.xsd.invoice.SignedInvoice;
import com.obiectumclaro.factronica.core.model.xsd.invoice.Tax;
import com.obiectumclaro.factronica.core.model.xsd.invoice.TaxInformation;
import com.obiectumclaro.factronica.core.util.DateUtil;

/**
 *
 * @author marco zaragocin
 */
@Stateless
public class InvoiceFactoryBean {

    @EJB
    private AgreementsBean agreementsBean;
    @EJB
    private JaxbMarshallerBean marshallerBean;
    
    private Properties issuerProperties;

    public byte[] generateXMLFile(Invoice xmlInvoice, Customer customer,Issuer issuer) {
        setIssuerProperties(agreementsBean.readSmtpConfig());
        try {
        
            SignedInvoice signedInvoice = new SignedInvoice();
            signedInvoice.setId("comprobante");
            AccessKeyBuilder akbuilder=getAccesKey(issuer);
            AccessKey accessKey=akbuilder.createAccessKey();
            String accessKeyString=accessKey.getFullKey();
            xmlInvoice.setAccesKey(accessKeyString);
            
            signedInvoice.setInfoTributaria(getTaxInformation(accessKey,issuer));
            signedInvoice.setInfoFactura(getInfoFactura(customer, xmlInvoice,issuer));
            signedInvoice.setVersion("1.1.0");
            signedInvoice.setDetalles(getDetalles(xmlInvoice));
            List<AdditionaInformationCustomer> addicionalInfoList=xmlInvoice.getAdditionalInfoList();
            if(!addicionalInfoList.isEmpty()){
            signedInvoice.setInfoAdicional(getInfoAdicional(addicionalInfoList));
            }
            
           return marshallerBean.marshall(signedInvoice);
            
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public AccessKeyBuilder getAccesKey(Issuer issuer){
         
        AccessKeyBuilder akbuilder= new AccessKeyBuilder();
        akbuilder.ruc(issuer.getRuc());
        akbuilder.serial("001001");
        akbuilder.sequence(generateSequence());      
       
        return akbuilder;
    }
    
    private TaxInformation getTaxInformation(AccessKey accessKey,Issuer issuer) {

    	issuer.setSocialReason(issuer.getSocialReason());
        issuer.setName(issuer.getName());
        issuer.setAddres(issuer.getAddres());
        //Se cambia el tipo de documento(Factura 01), el establecimiento (001), el punto de emision (001).
        TaxInformation taxInformation = new TaxInformation(accessKey.getFullKey(), issuer, issuer.getEnvironment().getDescripcion() ,
        		issuer.getEmision().getDescripcion(), "01", issuer.getEstablishment(),
                issuer.getPointOfSale(), accessKey.getSequence());
        return taxInformation;
    }

    private SignedInvoice.InfoFactura getInfoFactura(Customer customer, Invoice invoice,Issuer issuer) {
        SignedInvoice.InfoFactura infoFactura = new SignedInvoice.InfoFactura();
        infoFactura.setFechaEmision(DateUtil.getFormatedDate(new Date(), "dd/MM/yyyy"));
        //Aqui se tiene que poner si es contribuyente especial 
        infoFactura.setContribuyenteEspecial("001");
        infoFactura.setDirEstablecimiento(issuer.getAddres());
        infoFactura.setObligadoContabilidad(issuer.getAccountingLeads().getValor());
        infoFactura.setTipoIdentificacionComprador(getTipoIdentificacionComprador(customer.getIdType()));
        infoFactura.setIdentificacionComprador(customer.getId());
        infoFactura.setRazonSocialComprador(customer.getLegalName());
        infoFactura.setTotalSinImpuestos(invoice.getTotalExcludedTaxes().subtract(invoice.getTotalDiscount()));
        infoFactura.setTotalDescuento(invoice.getTotalDiscount());
        infoFactura.setPropina(invoice.getTip());
        //Aqui se tiene que poner la moneda 
        infoFactura.setMoneda("DOLAR");
        infoFactura.setImporteTotal((invoice.getTotalDue()).subtract(invoice.getTip()).setScale(2,BigDecimal.ROUND_UP));
        SignedInvoice.InfoFactura.TotalConImpuestos tc = new SignedInvoice.InfoFactura.TotalConImpuestos();
        for (TotalizedTax tt : invoice.getTotalizedTaxes()) {
            SignedInvoice.InfoFactura.TotalConImpuestos.TotalImpuesto totalImpuesto = new SignedInvoice.InfoFactura.TotalConImpuestos.TotalImpuesto();
            totalImpuesto.setCodigo(tt.getTaxCode().toString());
            totalImpuesto.setCodigoPorcentaje(tt.getRateCode());
            totalImpuesto.setTarifa(new BigDecimal(tt.getRate().toString()));
            totalImpuesto.setValor(tt.getTaxedValue().setScale(2, BigDecimal.ROUND_UP));
            totalImpuesto.setBaseImponible(tt.getTaxableBase().setScale(2, BigDecimal.ROUND_UP));
            tc.getTotalImpuesto().add(totalImpuesto);
        }
        infoFactura.setTotalConImpuestos(tc);
        return infoFactura;
    }
    
    private SignedInvoice.Detalles getDetalles(final Invoice invoice){
        
        SignedInvoice.Detalles detalles= new SignedInvoice.Detalles();
        for(InvoiceItem item: invoice.getItems()){
            SignedInvoice.Detalles.Detalle detalle= new SignedInvoice.Detalles.Detalle();
            detalle.setCantidad(item.getProduct().getAmount());
            
            //Bug Parameter INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL true in web.xml
            //http://stackoverflow.com/questions/6304025/work-around-for-faulty-interpret-empty-string-submitted-values-as-null-in-mojarr
            //http://forum.primefaces.org/viewtopic.php?f=3&t=22667
            //http://code.google.com/p/primefaces/issues/detail?id=4083
            final String alternateCode = item.getProduct().getAlternateCode();
			if (alternateCode != null && alternateCode.equals("")) {
                detalle.setCodigoAuxiliar(null);
            }else{
                detalle.setCodigoAuxiliar(alternateCode);
            }
            
            detalle.setCodigoPrincipal(item.getProduct().getCode());
            detalle.setDescripcion(item.getProduct().getName());
            detalle.setDescuento(item.getProduct().getDiscount());
            detalle.setPrecioUnitario(item.getProduct().getUnitPrice());
            detalles.getDetalle().add(detalle);
            SignedInvoice.Detalles.Detalle.Impuestos impuestos= new SignedInvoice.Detalles.Detalle.Impuestos();
            final BigDecimal precioTotalSinImpuestos = item.getProduct().getUnitPrice().multiply(item.getAmount()).subtract(detalle.getDescuento());
            detalle.setPrecioTotalSinImpuesto(precioTotalSinImpuestos.setScale(2,BigDecimal.ROUND_UP));
            for(TaxValue taxValue: item.getProduct().getTaxValueList()){
                Tax tax= new Tax();
                tax.setBaseImponible(item.getTotal().subtract(detalle.getDescuento()));
                tax.setCodigo(taxValue.getTaxId().getCode().toString());
                tax.setCodigoPorcentaje(taxValue.getTaxValueCode());
                tax.setValor(precioTotalSinImpuestos.multiply(taxValue.getRate().divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_UP));
                tax.setTarifa(taxValue.getRate());      
                impuestos.getImpuesto().add(tax);
            }
            detalle.setImpuestos(impuestos);
        }
        return detalles;
    }
    
    public SignedInvoice.InfoAdicional getInfoAdicional(final List<AdditionaInformationCustomer> infoAdicional) {
    	final SignedInvoice.InfoAdicional info = new SignedInvoice.InfoAdicional();
    	SignedInvoice.InfoAdicional.CampoAdicional campo;
    	for (AdditionaInformationCustomer additionaInformationCustomer : infoAdicional) {
    		campo = new SignedInvoice.InfoAdicional.CampoAdicional();
    		campo.setNombre(additionaInformationCustomer.getValue());
    		campo.setValue(additionaInformationCustomer.getDescription());
    		info.getCampoAdicional().add(campo);
		}
    	return info;
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
    
    public String getTipoIdentificacionComprador(IdType id){
        if(IdType.RUC.equals(id)){
            return "04";
        }
        if(IdType.CEDULA.equals(id)){
           return "05";
        }
        if(IdType.PASAPORTE.equals(id)){
            return "06";
        }
        return "07";
    }
    
    public String generateSequence(){
    Random rand = new Random();
        Integer numNoRange = Math.abs(rand.nextInt(900000000)+100000000);
        return numNoRange.toString();
    }
}
