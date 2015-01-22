/**
 * 
 */
package com.obiectumclaro.factronica.core.model;

import com.obiectumclaro.factronica.core.enumeration.DocumentType;
import com.obiectumclaro.factronica.core.web.service.sri.client.AuthorizationState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author rxvallejoc
 *
 */
@Entity
@Table(name = "Document")
public class Document {
	
	@Id
	@GeneratedValue
	private Long id;
	@Size(max = 49)
	private String accessKey;
	@Column(nullable=true)
	private String authorizationNumber;
	@Temporal(TemporalType.DATE)
	private Date emisionDate;
	@Enumerated(EnumType.STRING)
	private DocumentType documentType;
	@Lob
	private byte[] signedXml;
    @Lob
    @Column(nullable=true)
    private byte[] documentPdf;
	@JoinColumn(name = "issuer_pk", referencedColumnName = "pk")
	@ManyToOne(optional=false)
	private Issuer issuer;
	@Enumerated(EnumType.STRING)
	private AuthorizationState state;
	
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    public Date getEmisionDate() {
        return emisionDate;
    }

    public void setEmisionDate(Date emisionDate) {
        this.emisionDate = emisionDate;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public byte[] getSignedXml() {
        return signedXml;
    }

    public void setSignedXml(byte[] signedXml) {
        this.signedXml = signedXml;
    }

    public Issuer getIssuer() {
        return issuer;
    }

    public byte[] getDocumentPdf() {
        return documentPdf;
    }

    public void setDocumentPdf(byte[] documentPdf) {
        this.documentPdf = documentPdf;
    }

    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
    }
    
	public AuthorizationState getState() {
		return state;
	}
	public void setState(AuthorizationState state) {
		this.state = state;
	}
	
	
	
}
