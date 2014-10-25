/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author maza261109
 */
@Entity
@Table(name = "AdditionalProductInformation")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "AdditionalProductInformation.findAll", query = "SELECT a FROM AdditionalProductInformation a"),
		@NamedQuery(name = "AdditionalProductInformation.findByPk", query = "SELECT a FROM AdditionalProductInformation a WHERE a.pk = :pk"),
		@NamedQuery(name = "AdditionalProductInformation.findByProduct", query = "select a from AdditionalProductInformation a where a.product.pk = ?") })
public class AdditionalProductInformation implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "pk")
	@GeneratedValue
	private Long pk;
	@Size(max = 255)
	@Column(name = "value")
	private String value;
	@Size(max = 255)
	@Column(name = "name")
	private String name;
	@JoinColumn(name = "product_pk", referencedColumnName = "pk")
	@ManyToOne
	private Product product;

	public AdditionalProductInformation() {
	}

	public AdditionalProductInformation(Product product) {
		this.product = product;
	}

	public AdditionalProductInformation(Long pk) {
		this.pk = pk;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product
	 *            the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (pk != null ? pk.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof AdditionalProductInformation)) {
			return false;
		}
		AdditionalProductInformation other = (AdditionalProductInformation) object;
		if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.obiectumclaro.factronica.core.model.AdditionalProductInformation[ pk=" + pk + " ]";
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameLabel() {
		return name == null ? "..." : name;
	}

	public String getValueLabel() {
		return value == null ? "..." : value;
	}

}