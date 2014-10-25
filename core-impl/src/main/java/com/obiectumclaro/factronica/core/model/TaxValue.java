/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.obiectumclaro.factronica.core.enumeration.TaxCode;

/**
 * 
 * @author marco zaragocin
 */
@Entity
@Table(name = "TaxValue")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "TaxValue.findAll", query = "SELECT t FROM TaxValue t"),
		@NamedQuery(name = "TaxValue.findByPk", query = "SELECT t FROM TaxValue t WHERE t.pk = :pk"),
		@NamedQuery(name = "TaxValue.findByCode", query = "SELECT t FROM TaxValue t WHERE t.taxValueCode = :code") })
public class TaxValue implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long pk;
	private String taxValueCode;
	private String description;
	private BigDecimal taxable;
	private BigDecimal rate;
	private Date satartDate;
	private Date endDate;
	private Tax taxId;

	private BigDecimal value;

	public TaxValue() {
	}

	public TaxValue(final Long pk) {
		this.pk = pk;
	}

	/**
	 * @return {@link Boolean#TRUE} if the referenced {@link Tax}'s code is
	 *         {@link TaxCode#ICE}
	 */
	@Transient
	public boolean isICE() {
		return TaxCode.ICE.code.equals(getTaxId().getCode());
	}

	/**
	 * @return {@link Boolean#TRUE} if the referenced {@link Tax}'s code is
	 *         {@link TaxCode#IVA}
	 */
	@Transient
	public boolean isIVA() {
		return TaxCode.IVA.code.equals(getTaxId().getCode());
	}

	@Id
	@NotNull
	@Column(name = "pk")
	@GeneratedValue
	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	@Size(max = 255)
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "endDate")
	@Temporal(TemporalType.DATE)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return rate to be applied to the taxable value.
	 */
	@Column(name = "rate")
	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rateRetention) {
		this.rate = rateRetention;
	}

	@Column(name = "satartDate")
	@Temporal(TemporalType.DATE)
	public Date getSatartDate() {
		return satartDate;
	}

	public void setSatartDate(Date satartDate) {
		this.satartDate = satartDate;
	}

	/**
	 * @return amount over which the tax is applied.
	 */
	@Column(name = "taxable")
	public BigDecimal getTaxable() {
		return taxable;
	}

	public void setTaxable(BigDecimal taxable) {
		this.taxable = taxable;
	}

	@ManyToOne
	@XmlTransient
	public Tax getTaxId() {
		return taxId;
	}

	public void setTaxIdpk(Tax taxId) {
		this.taxId = taxId;
	}

	@Column(name = "tax_value_code")
	public String getTaxValueCode() {
		return taxValueCode;
	}

	public void setTaxValueCode(String taxValueCode) {
		this.taxValueCode = taxValueCode;
	}

	public void setTaxId(Tax taxId) {
		this.taxId = taxId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TaxValue [pk=").append(pk).append(", taxValueCode=").append(taxValueCode).append(", description=")
				.append(description).append(", taxable=").append(taxable).append(", rate=").append(rate).append(", satartDate=")
				.append(satartDate).append(", endDate=").append(endDate).append(", productList=").append(", taxId=")
				.append(taxId).append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rate == null) ? 0 : rate.hashCode());
		result = prime * result + ((taxId == null) ? 0 : taxId.hashCode());
		result = prime * result + ((taxValueCode == null) ? 0 : taxValueCode.hashCode());
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		TaxValue other = (TaxValue) obj;
		if (rate == null) {
			if (other.rate != null) {
				return false;
			}
		} else if (!rate.equals(other.rate)) {
			return false;
		}
		if (taxId == null) {
			if (other.taxId != null) {
				return false;
			}
		} else if (!taxId.equals(other.taxId)) {
			return false;
		}
		if (taxValueCode == null) {
			if (other.taxValueCode != null) {
				return false;
			}
		} else if (!taxValueCode.equals(other.taxValueCode)) {
			return false;
		}
		return true;
	}

	@Transient
	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
}
