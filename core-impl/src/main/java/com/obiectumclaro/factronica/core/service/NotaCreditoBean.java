/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.service;

import com.obiectumclaro.factronica.core.access.key.AccessKey;
import com.obiectumclaro.factronica.core.access.key.AccessKeyBuilder;
import com.obiectumclaro.factronica.core.enumeration.DocumentType;
import com.obiectumclaro.factronica.core.model.xsd.nota.credito.InfoTributaria;
import com.obiectumclaro.factronica.core.model.xsd.nota.credito.NotaCredito;
import com.obiectumclaro.factronica.core.util.DateUtil;

import java.io.File;
import java.util.Properties;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

/**
 *
 * @author marco
 */
@Stateless
public class NotaCreditoBean {

    @EJB
    InvoiceFactoryBean xmlGenerate;
    @EJB
    AgreementsBean agreementsBean;
    
    private Properties issuerProperties;
    
    
    
    public NotaCredito generarNotaCredito(NotaCredito notacredito) {
        issuerProperties= agreementsBean.readSmtpConfig();
        notacredito.setId("comprobante");
        AccessKeyBuilder akbuilder = getAccesKey(notacredito.getInfoTributaria(),notacredito.getInfoNotaCredito());
        AccessKey accessKey = akbuilder.createAccessKey();
       
        notacredito.getInfoTributaria().setClaveAcceso(accessKey.getFullKey());
        notacredito.getInfoTributaria().setTipoEmision("1");
        notacredito.getInfoTributaria().setSecuencial(accessKey.getSequence());
        notacredito.getInfoTributaria().setEstab(getIssuerProperties().getProperty("issuer.infoTributaria.estab"));
        notacredito.getInfoTributaria().setPtoEmi(getIssuerProperties().getProperty("issuer.infoTributaria.ptoEmi"));
        notacredito.getInfoNotaCredito().setFechaEmisionDocSustento(DateUtil.getFormatedDate(notacredito.getInfoNotaCredito().getFechaEmisionDocSus(), "dd/MM/yyyy"));
        notacredito.getInfoNotaCredito().setFechaEmision(DateUtil.getFormatedDate(notacredito.getInfoNotaCredito().getFechaEmi(), "dd/MM/yyyy"));
        try{
        File fileXML = new File("factura.xml");
        JAXBContext context = JAXBContext.newInstance(NotaCredito.class);
        Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(notacredito, System.out);
            m.marshal(notacredito, fileXML);
            notacredito.setFile(fileXML);
        }catch(Exception e){
            e.printStackTrace();
        }
        return notacredito;
    }

    public AccessKeyBuilder getAccesKey(InfoTributaria infotributaria,NotaCredito.InfoNotaCredito infoNotaCredito) {
        AccessKeyBuilder akbuilder = new AccessKeyBuilder();
        akbuilder.ruc(infotributaria.getRuc());
        akbuilder.type(DocumentType.NOTA_CREDITO);
        akbuilder.serial("001001");
        akbuilder.issued(infoNotaCredito.getFechaEmi());
        
        akbuilder.sequence(xmlGenerate.generateSequence());
        return akbuilder;
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
}
