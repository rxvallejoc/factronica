package com.obiectumclaro.factronica.core.sign;

import com.obiectumclaro.factronica.api.invoice.exception.SignerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import xades4j.XAdES4jException;
import xades4j.algorithms.EnvelopedSignatureTransform;
import xades4j.production.DataObjectReference;
import xades4j.production.SignedDataObjects;
import xades4j.production.SignerBESSri;
import xades4j.production.XadesBesSigningProfileSri;
import xades4j.properties.DataObjectDesc;
import xades4j.properties.DataObjectFormatProperty;
import xades4j.providers.KeyingDataProvider;
import xades4j.providers.impl.FileSystemKeyStoreKeyingDataProvider;
import xades4j.utils.DOMHelper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStoreException;


@Stateless
@LocalBean
public class SignerBean {

    public static final String MIME_TYPE = "text/xml";
    public static final String ENCONDING = "UTF-8";
    public static final String DESCRIPCION = "SRI - Obiectumclaro Library";
    public static final String KEY_STORE = "pkcs12";
    public static final String ROOT_ELEMENT_ID = "id";

    public byte[] sign(final byte[] document, final byte[] certificate, final String password) throws SignerException {
        KeyingDataProvider keyingProvider;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(certificate);
            keyingProvider = new FileSystemKeyStoreKeyingDataProvider(KEY_STORE, byteArrayInputStream,
                    new CertificateSelector(), new PasswordProvider(password),
                    new PasswordProvider(password), false);
            Document documet = getDocument(document);
            Element elemToSign = documet.getDocumentElement();
            SignerBESSri signer = (SignerBESSri) new XadesBesSigningProfileSri(keyingProvider).newSigner();
            DataObjectDesc dataObjectDesc = new DataObjectReference('#' + elemToSign.getAttribute(ROOT_ELEMENT_ID))
                    .withTransform(new EnvelopedSignatureTransform()).withDataObjectFormat(
                            new DataObjectFormatProperty(MIME_TYPE, ENCONDING).withDescription(DESCRIPCION));
            SignedDataObjects dataObjs = new SignedDataObjects(dataObjectDesc);
            signer.sign(dataObjs, elemToSign);

            TransformerFactory tf = TransformerFactory.newInstance();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            StreamResult streamResult = new StreamResult(byteArrayOutputStream);
            tf.newTransformer().transform(new DOMSource(documet), streamResult);

            return getDocumentBytes(documet);

        } catch (KeyStoreException e) {
            throw new SignerException(e);
        } catch (SAXException e) {
            throw new SignerException(e);
        } catch (IOException e) {
            throw new SignerException(e);
        } catch (ParserConfigurationException e) {
            throw new SignerException(e);
        } catch (XAdES4jException e) {
            throw new SignerException(e);
        } catch (TransformerConfigurationException e) {
            throw new SignerException(e);
        } catch (TransformerException e) {
            throw new SignerException(e);
        }

    }

    /**
     * @param byteDocument
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    private Document getDocument(byte[] byteDocument) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputStream inputStream = new ByteArrayInputStream(byteDocument);
        Document document = db.parse(inputStream);
        Element element = document.getDocumentElement();
        DOMHelper.useIdAsXmlIdSri(element);
        return document;
    }

    /**
     * @param document
     * @return
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    private byte[] getDocumentBytes(Document document) throws TransformerConfigurationException, TransformerException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        StreamResult streamResult = new StreamResult(byteArrayOutputStream);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.newTransformer().transform(new DOMSource(document), streamResult);
        return byteArrayOutputStream.toByteArray();
    }




}
