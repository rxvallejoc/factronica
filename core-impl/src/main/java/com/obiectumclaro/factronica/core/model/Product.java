/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.obiectumclaro.factronica.core.enumeration.ProductType;

/**
 *
 * @author maza261109
 */
@Entity
@Table(name = "Product", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"code"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "select p from Product p"),
    @NamedQuery(name = "Product.findByCode", query = "select p from Product p where p.code = ?"),
    @NamedQuery(name = "Product.findByPk", query = "select p from Product p where p.pk = ?")})
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "pk")
    @GeneratedValue
    private Long pk;
    @Size(max = 255)
    @Column(name = "code")
    private String code;
    @Size(max = 255)
    @Column(name = "alternateCode",nullable=true)
    private String alternateCode;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "productType")
    private ProductType productType;
    @Column(name = "unitPrice")
    private BigDecimal unitPrice;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TaxValue> taxValueList;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdditionalProductInformation> additionalProductInformationList;
    @Transient
    private BigDecimal amount;
    @Transient
    private BigDecimal discount;
    @Transient
    private BigDecimal total;
   

    public Product() {
        taxValueList = new ArrayList<TaxValue>();
        additionalProductInformationList = new ArrayList<AdditionalProductInformation>();
        amount=BigDecimal.ZERO;
        discount=BigDecimal.ZERO;
        total=BigDecimal.ZERO;
    }

    public Product(final Long pk) {
        this();
        this.pk = pk;
    }

    public Product(final String code) {
        this();
        this.code = code;
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public String getAlternateCode() {
        return alternateCode;
    }

    public void setAlternateCode(String alternateCode) {
        this.alternateCode = alternateCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @XmlTransient
    public List<TaxValue> getTaxValueList() {
        return taxValueList;
    }

    public void setTaxValueList(List<TaxValue> taxValueList) {
        this.taxValueList = taxValueList;
    }

    public void addTaxValue(final TaxValue taxValue) {
        this.taxValueList.add(taxValue);
    }

    @XmlTransient
    public List<AdditionalProductInformation> getAdditionalProductInformationList() {
        return additionalProductInformationList;
    }

    public void setAdditionalProductInformationList(List<AdditionalProductInformation> additionalProductInformationList) {
        this.additionalProductInformationList = additionalProductInformationList;
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
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the discount
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Product [alternateCode=").append(alternateCode)
                .append(", code=").append(code).append(", name=").append(name)
                .append(", productType=").append(productType)
                .append(", unitPrice=").append(unitPrice)
                .append(", taxValueList=").append(taxValueList).append("]");
        return builder.toString();
    }
}
