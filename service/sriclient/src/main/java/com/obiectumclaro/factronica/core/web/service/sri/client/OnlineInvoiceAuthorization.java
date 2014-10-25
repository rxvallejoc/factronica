package com.obiectumclaro.factronica.core.web.service.sri.client;

import com.obiectumclaro.factronica.core.web.service.sri.client.authorization.Autorizacion;
import com.obiectumclaro.factronica.core.web.service.sri.client.authorization.AutorizacionComprobantes;
import com.obiectumclaro.factronica.core.web.service.sri.client.authorization.AutorizacionComprobantesService;
import com.obiectumclaro.factronica.core.web.service.sri.client.authorization.RespuestaComprobante;
import com.obiectumclaro.factronica.core.web.service.sri.client.reception.Comprobante;
import com.obiectumclaro.factronica.core.web.service.sri.client.reception.RecepcionComprobantes;
import com.obiectumclaro.factronica.core.web.service.sri.client.reception.RecepcionComprobantesService;
import com.obiectumclaro.factronica.core.web.service.sri.client.reception.RespuestaSolicitud;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@Stateful
public class OnlineInvoiceAuthorization implements InvoiceAuthorization {

    private RespuestaSolicitud requestResponse;
    private RespuestaComprobante queryResponse;

    private List<Comprobante> comprobantes;
    private List<Autorizacion> autorizaciones;

    @PostConstruct
    public void setup() {
        comprobantes = new ArrayList<>();
        autorizaciones = new ArrayList<>();
    }

    @Override
    public AuthorizationState request(final byte[] signedInvoice) {
        return request(signedInvoice, ServiceEnvironment.PRODUCTION);
    }

    @Override
    public AuthorizationState request(final byte[] signedInvoice, final ServiceEnvironment env) {
        final RecepcionComprobantesService service = startRecepcionComprobantesService(env);
        final RecepcionComprobantes port = service.getRecepcionComprobantesPort();
        requestResponse = port.validarComprobante(signedInvoice);
        comprobantes = requestResponse.getComprobantes().getComprobante();
        return AuthorizationState.valueOf(requestResponse.getEstado());
    }

    @Override
    public List<Autorizacion> syncRequest(final byte[] signedInvoice) throws ReturnedInvoiceException {
        return syncRequest(signedInvoice, ServiceEnvironment.PRODUCTION);
    }

    @Override
    public List<Autorizacion> syncRequest(final byte[] signedInvoice, final ServiceEnvironment env)
            throws ReturnedInvoiceException {
        return syncRequest(signedInvoice, env, DEFAULT_WAITING_TIME);
    }

    @Override
    public List<Autorizacion> syncRequest(final byte[] signedInvoice, final ServiceEnvironment env, final int seconds)
            throws ReturnedInvoiceException {
        final AuthorizationState response = request(signedInvoice, env);
        if (AuthorizationState.DEVUELTA.equals(response)) {
            throw new ReturnedInvoiceException(comprobantes);
        } else {
            waitFor(seconds);
            final String accessKey = readFrom(signedInvoice);
            return query(accessKey, env);
        }
    }

    @Override
    public List<Autorizacion> query(final String accessKey) {
        return query(accessKey, ServiceEnvironment.PRODUCTION);
    }


    @Override
    public List<Autorizacion> query(final String accessKey, final ServiceEnvironment env) {
        final AutorizacionComprobantesService service = startAutorizacionComprobantesService(env);
        final AutorizacionComprobantes port = service.getAutorizacionComprobantesPort();
        queryResponse = port.autorizacionComprobante(accessKey);
        autorizaciones = queryResponse.getAutorizaciones().getAutorizacion();
        return autorizaciones;
    }

    private RecepcionComprobantesService startRecepcionComprobantesService(final ServiceEnvironment environment) {
        try {
            final String wsdlLocation = environment.wsdlLocation + URL_REQUEST;
            return new RecepcionComprobantesService(new URL(wsdlLocation),
                    new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesService"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private AutorizacionComprobantesService startAutorizacionComprobantesService(final ServiceEnvironment environment) {
        try {
            final String wsdlLocation = environment.wsdlLocation + URL_QUERY;
            return new AutorizacionComprobantesService(new URL(wsdlLocation),
                    new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesService"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void waitFor(final int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            waitFor(seconds);
        }
    }

    private String readFrom(final byte[] signedInvoice) {
        try {
            final String path = "/*/infoTributaria/claveAcceso";
            final XPathFactory xFactory = XPathFactory.newInstance();
            final XPath xpath = xFactory.newXPath();
            final XPathExpression expression = xpath.compile(path);

            final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            final Document xmlDocument = docBuilder.parse(new InputSource(new ByteArrayInputStream(signedInvoice)));

            return (String) expression.evaluate(xmlDocument, XPathConstants.STRING);
        } catch (XPathExpressionException xee) {
            throw new RuntimeException(xee);
        } catch (ParserConfigurationException pce) {
            throw new RuntimeException(pce);
        } catch (SAXException se) {
            throw new RuntimeException(se);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @Override
    public List<Comprobante> getComprobantes() {
        return comprobantes;
    }

}
