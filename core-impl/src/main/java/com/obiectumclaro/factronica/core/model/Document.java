/**
 * 
 */
package com.obiectumclaro.factronica.core.model;

import java.util.Date;

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

import com.obiectumclaro.factronica.core.enumeration.DocumentType;
import com.obiectumclaro.factronica.core.web.service.sri.client.AuthorizationState;

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
	@JoinColumn(name = "issuer_pk", referencedColumnName = "pk")
	@ManyToOne(optional=false)
	private Issuer issuer;
	@Enumerated(EnumType.STRING)
	private AuthorizationState state;
	
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * @return the accessKey
     */
    public String getAccessKey() {
        return accessKey;
    }
    /**
     * @param accessKey the accessKey to set
     */
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    /**
     * @return the authorizationNumber
     */
    public String getAuthorizationNumber() {
        return authorizationNumber;
    }
    /**
     * @param authorizationNumber the authorizationNumber to set
     */
    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }
    /**
     * @return the emisionDate
     */
    public Date getEmisionDate() {
        return emisionDate;
    }
    /**
     * @param emisionDate the emisionDate to set
     */
    public void setEmisionDate(Date emisionDate) {
        this.emisionDate = emisionDate;
    }
    /**
     * @return the documentType
     */
    public DocumentType getDocumentType() {
        return documentType;
    }
    /**
     * @param documentType the documentType to set
     */
    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }
    /**
     * @return the signedXml
     */
    public byte[] getSignedXml() {
        return signedXml;
    }
    /**
     * @param signedXml the signedXml to set
     */
    public void setSignedXml(byte[] signedXml) {
        this.signedXml = signedXml;
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
    
	public AuthorizationState getState() {
		return state;
	}
	public void setState(AuthorizationState state) {
		this.state = state;
	}
	
	
	
}
