/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author marco zaragocin
 */
@Entity
@Table(name = "Tax")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Tax.findAll", query = "SELECT t FROM Tax t"),
		@NamedQuery(name = "Tax.findByPk", query = "SELECT t FROM Tax t WHERE t.pk = :pk"),
		@NamedQuery(name = "Tax.findByDescription", query = "SELECT t FROM Tax t WHERE t.description = :description"),
		@NamedQuery(name = "Tax.findByStateTax", query = "SELECT t FROM Tax t WHERE t.stateTax = :stateTax") })
public class Tax implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "pk")
	private Long pk;

	@NotNull
	@Column(name = "code")
	private Long code;

	@Size(max = 255)
	@Column(name = "description")
	private String description;
	@Size(max = 255)
	@Column(name = "stateTax")
	private String stateTax;

	// Propiedad para la administracion de productos
	@Transient
	private Long taxValueId;

	public Tax() {
	}

	public Tax(Long pk) {
		this.pk = pk;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStateTax() {
		return stateTax;
	}

	public void setStateTax(String stateTax) {
		this.stateTax = stateTax;
	}

	/**
	 * @return the tax's code assigned by the SRI.
	 */
	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public Long getTaxValueId() {
		return taxValueId;
	}

	public void setTaxValueId(Long taxValueId) {
		this.taxValueId = taxValueId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Tax)) {
			return false;
		}
		Tax other = (Tax) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Tax [pk=").append(pk).append(", code=").append(code)
				.append(", description=").append(description).append("]");
		return builder.toString();
	}

}
