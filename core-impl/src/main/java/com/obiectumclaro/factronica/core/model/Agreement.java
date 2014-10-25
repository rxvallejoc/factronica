/**
 * 
 */
package com.obiectumclaro.factronica.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.obiectumclaro.factronica.core.enumeration.AgreementStatus;

/**
 * @author faustodelatog
 * 
 */
@Entity
public class Agreement implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long pk;
	@Temporal(TemporalType.DATE)
	private Date lastDate;
	@Enumerated(EnumType.STRING)
	private AgreementStatus status;
	// @Lob
	// private byte[] document;
	private String token;
	private int duration;

	public Agreement() {

	}

	public Agreement(Date lastDate, AgreementStatus status, String token, int duration) {
		super();
		this.lastDate = lastDate;
		this.status = status;
		this.token = token;
		this.duration = duration;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date date) {
		this.lastDate = date;
	}

	public AgreementStatus getStatus() {
		return status;
	}

	public void setStatus(AgreementStatus status) {
		this.status = status;
	}

	// public byte[] getDocument() {
	// return document;
	// }
	//
	// public void setDocument(byte[] document) {
	// this.document = document;
	// }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
