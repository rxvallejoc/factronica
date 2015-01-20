package com.obiectumclaro.factronica.core.emac;


import com.obiectumclaro.factronica.core.access.key.AccessKey;
import com.obiectumclaro.factronica.core.access.key.AccessKeyBuilder;
import com.obiectumclaro.factronica.core.emac.model.DetalleDocumentoSri;
import com.obiectumclaro.factronica.core.emac.model.DocumentoSri;
import com.obiectumclaro.factronica.core.emac.timer.EmacDocumentSubmissionMessage;
import com.obiectumclaro.factronica.core.model.Issuer;
import com.obiectumclaro.factronica.core.model.xsd.invoice.SignedInvoice;
import com.obiectumclaro.factronica.core.model.xsd.invoice.Tax;
import com.obiectumclaro.factronica.core.model.xsd.invoice.TaxInformation;
import com.obiectumclaro.factronica.core.service.JaxbMarshallerBean;
import com.obiectumclaro.factronica.core.util.DateUtil;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

@Stateless
public class InvoiceBuilder {

    private static final Logger LOG = Logger.getLogger(InvoiceBuilder.class);

    @EJB
    private JaxbMarshallerBean marshallerBean;
    static final NumberFormat FORMATTER_DECIMAL = new DecimalFormat("000000000");


    public EmacDocumentSubmissionMessage generateXMLFile(DocumentoSri sriDocument,Issuer issuer) {
        try {

            SignedInvoice signedInvoice = new SignedInvoice();
            signedInvoice.setId("comprobante");
            AccessKeyBuilder akbuilder = getAccesKey(sriDocument,issuer);
            AccessKey accessKey=akbuilder.createAccessKey();
            String accessKeyString=accessKey.getFullKey();
            sriDocument.setClaveacceso(accessKeyString);

            signedInvoice.setInfoTributaria(getTaxInformation(accessKey,issuer));
            signedInvoice.setInfoFactura(getInfoFactura(sriDocument,issuer));
            signedInvoice.setVersion("1.1.0");
            signedInvoice.setDetalles(getDetalles(sriDocument));
//            List<AdditionaInformationCustomer> addicionalInfoList = sriDocument.getAdditionalInfoList();
//            if(!addicionalInfoList.isEmpty()){
//                signedInvoice.setInfoAdicional(getInfoAdicional(addicionalInfoList));
//            }

            byte[] invoice = marshallerBean.marshall(signedInvoice);
            return getEmacDocumentSubmissionMessage(sriDocument, issuer, accessKeyString, invoice);

        } catch (JAXBException exception) {
            LOG.error("Fail JAXB Conversion",exception);

        } catch (IOException exception) {
            LOG.error("Fail reading document",exception);
        }
        return null;
    }

    private EmacDocumentSubmissionMessage getEmacDocumentSubmissionMessage(DocumentoSri sriDocument, Issuer issuer, String accessKeyString, byte[] invoice) {
        EmacDocumentSubmissionMessage message = new EmacDocumentSubmissionMessage();
        message.setDocument(invoice);
        message.setAccessKey(accessKeyString);
        message.setIssuer(issuer);
        message.setRequestedOn(new Date());
        message.setSriDocument(sriDocument);
        return message;
    }

    public AccessKeyBuilder getAccesKey(DocumentoSri sriDocument,Issuer issuer){

        AccessKeyBuilder accessKeyBuilder= new AccessKeyBuilder();
        accessKeyBuilder.ruc(issuer.getRuc());
        accessKeyBuilder.serial(sriDocument.getEstab() + sriDocument.getPtoemi());
        accessKeyBuilder.sequence(FORMATTER_DECIMAL.format(Integer.parseInt(sriDocument.getSecuencial())));

        return accessKeyBuilder;
    }

    private TaxInformation getTaxInformation(AccessKey accessKey, Issuer issuer) {
        issuer.setSocialReason(issuer.getSocialReason());
        issuer.setName(issuer.getName());
        issuer.setAddres(issuer.getAddres());
        //Se cambia el tipo de documento(Factura 01), el establecimiento (001), el punto de emision (001).
        TaxInformation taxInformation = new TaxInformation(accessKey.getFullKey(), issuer, issuer.getEnvironment().getDescripcion() ,
                issuer.getEmision().getDescripcion(), "01", issuer.getEstablishment(),
                issuer.getPointOfSale(), accessKey.getSequence());
        return taxInformation;
    }

    private SignedInvoice.InfoFactura getInfoFactura(DocumentoSri sriDocument, Issuer issuer) {
        SignedInvoice.InfoFactura infoFactura = new SignedInvoice.InfoFactura();
        infoFactura.setFechaEmision(DateUtil.getFormatedDate(new Date(), "dd/MM/yyyy"));
        //Aqui se tiene que poner si es contribuyente especial
        infoFactura.setContribuyenteEspecial("001");
        infoFactura.setDirEstablecimiento(issuer.getAddres());
        infoFactura.setObligadoContabilidad(issuer.getAccountingLeads().getValor());
        infoFactura.setTipoIdentificacionComprador(sriDocument.getTipoidentificacion());
        infoFactura.setIdentificacionComprador(sriDocument.getIdentificacion());
        infoFactura.setRazonSocialComprador(sriDocument.getRazonsocial1());
        infoFactura.setTotalSinImpuestos(sriDocument.getTotalsinimpuestos());
        infoFactura.setTotalDescuento(sriDocument.getTotaldescuento());
        infoFactura.setPropina(sriDocument.getPropina());
        //Aqui se tiene que poner la moneda
        infoFactura.setMoneda(sriDocument.getMoneda());
        infoFactura.setImporteTotal(sriDocument.getImportetotal());
        SignedInvoice.InfoFactura.TotalConImpuestos tc = new SignedInvoice.InfoFactura.TotalConImpuestos();

            SignedInvoice.InfoFactura.TotalConImpuestos.TotalImpuesto totalImpuesto = new SignedInvoice.InfoFactura.TotalConImpuestos.TotalImpuesto();
            totalImpuesto.setCodigo("2");
            totalImpuesto.setCodigoPorcentaje("2");
            totalImpuesto.setTarifa(BigDecimal.valueOf(12L));
            totalImpuesto.setValor(sriDocument.getTotalimpuestos());
            totalImpuesto.setBaseImponible(sriDocument.getTotalsinimpuestos());
            tc.getTotalImpuesto().add(totalImpuesto);

        infoFactura.setTotalConImpuestos(tc);
        return infoFactura;
    }

    private SignedInvoice.Detalles getDetalles(final DocumentoSri sriDocument){

        SignedInvoice.Detalles detalles= new SignedInvoice.Detalles();
        for(DetalleDocumentoSri item: sriDocument.getDetalleDocumentoSri()){
            SignedInvoice.Detalles.Detalle detalle= new SignedInvoice.Detalles.Detalle();
            detalle.setCantidad(item.getCantidad());

            //Bug Parameter INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL true in web.xml
            //http://stackoverflow.com/questions/6304025/work-around-for-faulty-interpret-empty-string-submitted-values-as-null-in-mojarr
            //http://forum.primefaces.org/viewtopic.php?f=3&t=22667
            //http://code.google.com/p/primefaces/issues/detail?id=4083
            final String alternateCode = item.getCodAuxuliar();
            if (alternateCode != null && alternateCode.equals("")) {
                detalle.setCodigoAuxiliar(null);
            }else{
                detalle.setCodigoAuxiliar(alternateCode);
            }

            detalle.setCodigoPrincipal(item.getCodPrincipal());
            detalle.setDescripcion(item.getDescripcion());
            detalle.setDescuento(item.getDescuento());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            detalles.getDetalle().add(detalle);
            SignedInvoice.Detalles.Detalle.Impuestos impuestos= new SignedInvoice.Detalles.Detalle.Impuestos();
            //final BigDecimal precioTotalSinImpuestos = item.getProduct().getUnitPrice().multiply(item.getAmount()).subtract(detalle.getDescuento());
            detalle.setPrecioTotalSinImpuesto(item.getPrecioTotalSnImpuesto());

                Tax tax= new Tax();
                tax.setBaseImponible(item.getPrecioTotalSnImpuesto());
                tax.setCodigo("2");
                tax.setCodigoPorcentaje("2");
                tax.setValor(item.getValorIva());
                tax.setTarifa(BigDecimal.valueOf(12L));
                impuestos.getImpuesto().add(tax);

            detalle.setImpuestos(impuestos);
        }
        return detalles;
    }

}

