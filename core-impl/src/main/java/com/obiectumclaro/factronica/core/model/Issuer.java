/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.obiectumclaro.factronica.core.enumeration.EmissionRate;
import com.obiectumclaro.factronica.core.enumeration.EnvironmentXML;
import com.obiectumclaro.factronica.core.enumeration.SiNo;

/**
 * 
 * @author marco zaragocin
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Issuer.findById", query = "SELECT c FROM Issuer c WHERE c.pk = ?") })
public class Issuer implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	private Long pk;
	private String name;
	private String description;
	private String addres;
	private String phoneNumbre;
	private String socialReason;
	private String contactMail;
	private String ruc;
    private String establishment;
    private String pointOfSale;
	@Enumerated(EnumType.STRING)
	private SiNo accountingLeads;
	@Enumerated(EnumType.STRING)
	private EnvironmentXML environment;
	@Enumerated(EnumType.STRING)
	private EmissionRate emision;
	@Lob
	private byte[] logo;
	@Lob
	private byte[] certificate;
	private String password;
	@Transient
	private Boolean accountingLeadsChk;
	@OneToMany(mappedBy="issuer")
	private List<Document> documents;

	public Issuer() {
	}

	public Issuer(Long pk, String name, String description, String addres,
			String phoneNumbre, String socialReason, String contactMail,
			String ruc) {
		this.pk = pk;
		this.name = name;
		this.description = description;
		this.addres = addres;
		this.phoneNumbre = phoneNumbre;
		this.socialReason = socialReason;
		this.contactMail = contactMail;
		this.ruc = ruc;
	}

	/**
	 * @return the pk
	 */

	public Long getPk() {
		return pk;
	}

	/**
	 * @param pk
	 *            the pk to set
	 */
	public void setPk(Long pk) {
		this.pk = pk;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the addres
	 */
	public String getAddres() {
		return addres;
	}

	/**
	 * @param addres
	 *            the addres to set
	 */
	public void setAddres(String addres) {
		this.addres = addres;
	}

	/**
	 * @return the phoneNumbre
	 */
	public String getPhoneNumbre() {
		return phoneNumbre;
	}

	/**
	 * @param phoneNumbre
	 *            the phoneNumbre to set
	 */
	public void setPhoneNumbre(String phoneNumbre) {
		this.phoneNumbre = phoneNumbre;
	}

	/**
	 * @return the socialReason
	 */
	public String getSocialReason() {
		return socialReason;
	}

	/**
	 * @param socialReason
	 *            the socialReason to set
	 */
	public void setSocialReason(String socialReason) {
		this.socialReason = socialReason;
	}

	/**
	 * @return the contactMail
	 */
	public String getContactMail() {
		return contactMail;
	}

	/**
	 * @param contactMail
	 *            the contactMail to set
	 */
	public void setContactMail(String contactMail) {
		this.contactMail = contactMail;
	}

	/**
	 * @return the ruc
	 */
	public String getRuc() {
		return ruc;
	}

	/**
	 * @param ruc
	 *            the ruc to set
	 */
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

    public String getEstablishment() {
        return establishment;
    }

    public void setEstablishment(String establishment) {
        this.establishment = establishment;
    }

    public String getPointOfSale() {
        return pointOfSale;
    }

    public void setPointOfSale(String pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    @Override
	public String toString() {
		return "Issuer [pk=" + pk + ", name=" + name + ", description="
				+ description + ", addres=" + addres + ", phoneNumbre="
				+ phoneNumbre + ", socialReason=" + socialReason
				+ ", contactMail=" + contactMail + ", ruc=" + ruc + "]";
	}

	/**
	 * @return the accountingLeads
	 */
	public SiNo getAccountingLeads() {
		return accountingLeads;
	}

	/**
	 * @param accountingLeads
	 *            the accountingLeads to set
	 */
	public void setAccountingLeads(SiNo accountingLeads) {
		this.accountingLeads = accountingLeads;
	}

	/**
	 * @return the accountingLeadsChk
	 */
	public Boolean getAccountingLeadsChk() {
		if (getAccountingLeads().equals(SiNo.SI)) {
			this.accountingLeadsChk = Boolean.TRUE;
			return accountingLeadsChk;
		}
		return Boolean.FALSE;
	}

	/**
	 * @param accountingLeadsChk
	 *            the accountingLeadsChk to set
	 */
	public void setAccountingLeadsChk(Boolean accountingLeadsChk) {
		this.accountingLeadsChk = accountingLeadsChk;
	}

	/**
	 * @return the environment
	 */
	public EnvironmentXML getEnvironment() {
		return environment;
	}

	/**
	 * @param environment
	 *            the environment to set
	 */
	public void setEnvironment(EnvironmentXML environment) {
		this.environment = environment;
	}

	/**
	 * @return the emision
	 */
	public EmissionRate getEmision() {
		return emision;
	}

	/**
	 * @param emision
	 *            the emision to set
	 */
	public void setEmision(EmissionRate emision) {
		this.emision = emision;
	}

	/**
	 * @return the documents
	 */
	public List<Document> getDocuments() {
		return documents;
	}

	/**
	 * @param documents the documents to set
	 */
	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

    /**
     * @return the logo
     */
    public byte[] getLogo() {
        return logo;
    }

    /**
     * @param logo the logo to set
     */
    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    /**
     * @return the certificate
     */
    public byte[] getCertificate() {
        return certificate;
    }

    /**
     * @param certificate the certificate to set
     */
    public void setCertificate(byte[] certificate) {
        this.certificate = certificate;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

	
	

}
