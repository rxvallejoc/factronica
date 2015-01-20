/**
 *
 */
package com.obiectumclaro.factronica.core.emac.timer;

import com.obiectumclaro.factronica.core.emac.model.DocumentoSri;
import com.obiectumclaro.factronica.core.model.Issuer;

import java.io.Serializable;
import java.util.Date;

public class EmacDocumentSubmissionMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    private Issuer issuer;
    private String accessKey;
    private DocumentoSri sriDocument;
    private byte[] document;
    private byte[] signedDocument;
    private String requestor;
    private Date requestedOn;

    public EmacDocumentSubmissionMessage() {

    }

    public EmacDocumentSubmissionMessage(Issuer issuer, String accessKey, DocumentoSri sriDocument, byte[] document, byte[] signedDocument, String requestor, Date requestedOn) {
        this.issuer = issuer;
        this.accessKey = accessKey;
        this.sriDocument = sriDocument;
        this.document = document;
        this.signedDocument = signedDocument;
        this.requestor = requestor;
        this.requestedOn = requestedOn;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Issuer getIssuer() {
        return issuer;
    }

    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public DocumentoSri getSriDocument() {
        return sriDocument;
    }

    public void setSriDocument(DocumentoSri sriDocument) {
        this.sriDocument = sriDocument;
    }

    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public byte[] getSignedDocument() {
        return signedDocument;
    }

    public void setSignedDocument(byte[] signedDocument) {
        this.signedDocument = signedDocument;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public Date getRequestedOn() {
        return requestedOn;
    }

    public void setRequestedOn(Date requestedOn) {
        this.requestedOn = requestedOn;
    }
}
